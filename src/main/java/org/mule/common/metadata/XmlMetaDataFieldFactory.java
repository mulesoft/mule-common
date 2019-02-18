package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.property.LabelMetaDataProperty;
import org.mule.common.metadata.property.QNameMetaDataProperty;
import org.mule.common.metadata.property.xml.AttributeMetaDataFieldProperty;
import org.mule.common.metadata.property.xml.SchemaTypeMetaDataProperty;
import org.mule.common.metadata.property.xml.UnboundMetaDataProperty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;

/**
 * Field Factory For XML Structured
 */
public class XmlMetaDataFieldFactory implements MetaDataFieldFactory
{

    private static final Map<QName, DataType> typeMapping = new HashMap<QName, DataType>();

    static
    {
        typeMapping.put(XmlConstants.XSD_BYTE, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_INT, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_INTEGER, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_SHORT, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_NEGATIVEINTEGER, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_NONNEGATIVEINTEGER, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_NONPOSITIVEINTEGER, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_POSITIVEINTEGER, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_UNSIGNEDBYTE, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_UNSIGNEDINT, DataType.INTEGER);
        typeMapping.put(XmlConstants.XSD_UNSIGNEDSHORT, DataType.INTEGER);

        typeMapping.put(XmlConstants.XSD_LONG, DataType.LONG);
        typeMapping.put(XmlConstants.XSD_UNSIGNEDLONG, DataType.LONG);

        typeMapping.put(XmlConstants.XSD_FLOAT, DataType.DOUBLE);
        typeMapping.put(XmlConstants.XSD_DOUBLE, DataType.DOUBLE);

        typeMapping.put(XmlConstants.XSD_BOOLEAN, DataType.BOOLEAN);

        typeMapping.put(XmlConstants.XSD_STRING, DataType.STRING);
        typeMapping.put(XmlConstants.XSD_ANY, DataType.STRING);

        typeMapping.put(XmlConstants.XSD_NORMALIZEDSTRING, DataType.STRING);

        typeMapping.put(XmlConstants.XSD_DECIMAL, DataType.DECIMAL);

        typeMapping.put(XmlConstants.XSD_DATE, DataType.DATE);
        typeMapping.put(XmlConstants.XSD_DATETIME, DataType.DATE_TIME);

        typeMapping.put(XmlConstants.XSD_HEXBIN, DataType.BYTE);
        typeMapping.put(XmlConstants.XSD_BASE64, DataType.BYTE);
    }

    public static final String TEXT = "text()";

    private SchemaProvider schemas;
    private QName rootElementName;


    private XmlMetaDataNamespaceManager namespaceManager;
    volatile private SchemaType rootType;

    public XmlMetaDataFieldFactory(SchemaProvider schemas, QName rootElementName, XmlMetaDataNamespaceManager namespaceManager)
    {
        this.schemas = schemas;
        this.rootElementName = rootElementName;
        this.namespaceManager = namespaceManager;
    }

    @Override
    public List<MetaDataField> createFields()
    {

        List<MetaDataField> metaDataFields = new ArrayList<MetaDataField>();
        final Map<SchemaType, XmlMetaDataModel> visitedTypes = new HashMap<SchemaType, XmlMetaDataModel>();
        SchemaType rootType = getRootType();
        if (rootType != null)
        {
            loadFields(rootType, metaDataFields, visitedTypes);
        }

        return metaDataFields;
    }

    protected void loadFields(SchemaType type, List<MetaDataField> metaDataFields, Map<SchemaType, XmlMetaDataModel> visitedTypes)
    {

        if (hasSimpleContent(type))
        {
            final DataType dataType = getDataType(type, DataType.STRING);
            metaDataFields.add(new DefaultMetaDataField(TEXT, new DefaultSimpleMetaDataModel(dataType), new QNameMetaDataProperty(new QName(TEXT))));
        }
        final SchemaProperty[] properties = type.getProperties();
        for (SchemaProperty property : properties)
        {
            final QName name = namespaceManager.assignPrefixIfNotPresent(property.getName());
            final SchemaType propertyType = property.getType();
            if (property.isAttribute())
            {
                final DataType dataType = getDataType(propertyType, DataType.STRING);
                final DefaultSimpleMetaDataModel model = new DefaultSimpleMetaDataModel(dataType);
                addSchemaType(propertyType, model);
                metaDataFields.add(new DefaultMetaDataField(toLabel(name), model, new QNameMetaDataProperty(name), new LabelMetaDataProperty(toAttributeLabel(name)), new AttributeMetaDataFieldProperty(true)));
            }
            else if (isList(property))
            {
                if (isSimpleType(propertyType))
                {
                    final DataType dataType = getDataType(propertyType, DataType.STRING);
                    final DefaultListMetaDataModel defaultListMetaDataModel = new DefaultListMetaDataModel(new DefaultSimpleMetaDataModel(dataType));
                    defaultListMetaDataModel.addProperty(getUnboundProperty(property));
                    addSchemaType(propertyType, defaultListMetaDataModel);
                    metaDataFields.add(new DefaultMetaDataField(toLabel(name), defaultListMetaDataModel, new QNameMetaDataProperty(name)));
                }
                else
                {
                    final XmlMetaDataModel model = buildXMLMetaDataModel(visitedTypes, propertyType);
                    final DefaultListMetaDataModel defaultListMetaDataModel = new DefaultListMetaDataModel(model);
                    addSchemaType(propertyType, defaultListMetaDataModel);
                    defaultListMetaDataModel.addProperty(getUnboundProperty(property));
                    metaDataFields.add(new DefaultMetaDataField(toLabel(name), defaultListMetaDataModel, new QNameMetaDataProperty(name)));
                }
            }
            else
            {
                if (isSimpleType(propertyType))
                {
                    final DataType dataType = getDataType(propertyType, DataType.STRING);
                    final DefaultSimpleMetaDataModel model = new DefaultSimpleMetaDataModel(dataType);
                    addSchemaType(propertyType, model);
                    metaDataFields.add(new DefaultMetaDataField(toLabel(name), model, new QNameMetaDataProperty(name)));
                }
                else
                {
                    final XmlMetaDataModel model = buildXMLMetaDataModel(visitedTypes, propertyType);
                    addSchemaType(propertyType, model);
                    metaDataFields.add(new DefaultMetaDataField(toLabel(name), model, new QNameMetaDataProperty(name)));
                }
            }
        }
    }

    public SchemaType getRootType()
    {
        if (rootType == null)
        {
            synchronized (this)
            {
                if (rootType == null)
                {
                    rootType = getRootTypeFromSchemas();
                }
            }
        }
        return rootType;
    }

    private SchemaType getRootTypeFromSchemas()
    {
        try
        {
            SchemaGlobalElement rootElement = schemas.findRootElement(rootElementName);

            if (rootElement != null)
            {
                return rootElement.getType();
            }
            else
            {
                return null;
            }
        }
        catch (XmlException e)
        {
            throw new MetaDataGenerationException(e);
        }
    }

    private void addSchemaType(SchemaType type, MetaDataModel metaDataModel)
    {
        final QName typeQName = type.getName();
        if (typeQName != null)
        {
            metaDataModel.addProperty(new SchemaTypeMetaDataProperty(typeQName));
        }
    }

    private String toAttributeLabel(QName name)
    {
        return "@" + toLabel(name);
    }


    private XmlMetaDataModel buildXMLMetaDataModel(Map<SchemaType, XmlMetaDataModel> visitedTypes, SchemaType propertyType)
    {
        XmlMetaDataModel model;
        if (visitedTypes.containsKey(propertyType))
        {
            model = visitedTypes.get(propertyType);
        }
        else
        {
            final ArrayList<MetaDataField> fields = new ArrayList<MetaDataField>();
            model = new DefaultXmlMetaDataModel(schemas, rootElementName, fields, isAllowsAnyFields(propertyType), namespaceManager);
            visitedTypes.put(propertyType, model);
            loadFields(propertyType, fields, visitedTypes);
        }

        return model;
    }

    public boolean isAllowsAnyFields(SchemaType propertyType)
    {
        boolean allowsAnyFields = false;
        // If an element defines only a complex type of any attributes but no explicit attributes,
        // it will cause the metaDataFields list to be empty, which is not true.
        final QName anyElement = new QName("mule", "##any");
        if (propertyType != null && propertyType.getElementSequencer().peek(anyElement))
        {
            allowsAnyFields = true;
        }
        return allowsAnyFields;
    }

    private String toLabel(QName name)
    {
        if (namespaceManager.isPrefixDeclared(name))
        {
            return name.getPrefix() + ":" + name.getLocalPart();
        }
        else
        {
            return name.getLocalPart();
        }
    }


    private boolean hasSimpleContentOnly(SchemaType type)
    {

        return hasSimpleContent(type) && isPlainElement(type);
    }

    private boolean hasSimpleContent(SchemaType type)
    {

        return type.isSimpleType() || type.getContentType() == SchemaType.SIMPLE_CONTENT || type.getContentType() == SchemaType.MIXED_CONTENT
               || type.getComponentType() == SchemaType.EMPTY_CONTENT;
    }

    private boolean isList(SchemaProperty property)
    {
        BigInteger maxOccurs = property.getMaxOccurs();
        return maxOccurs == null || property.getMaxOccurs().intValue() > 1;
    }

    private UnboundMetaDataProperty getUnboundProperty(SchemaProperty property)
    {
        final BigInteger maxOccurs = property.getMaxOccurs();
        final BigInteger minOccurs = property.getMinOccurs();
        int max = maxOccurs == null ? Integer.MAX_VALUE : maxOccurs.intValue();
        int min = minOccurs == null ? 0 : minOccurs.intValue();
        return new UnboundMetaDataProperty(min, max);
    }

    private DataType getDataType(SchemaType type, DataType defaultDataType)
    {
        SchemaType simpleType = getSimpleBaseType(type);

        do
        {
            DataType fieldType = typeMapping.get(simpleType.getName());
            if (fieldType != null)
            {
                return fieldType;
            }

            simpleType = simpleType.getBaseType();
        }
        while (simpleType != null);

        return defaultDataType;
    }

    private boolean isSimpleType(SchemaType type)
    {
        return getDataType(type, null) != null && hasSimpleContentOnly(type);
    }

    /**
     * Element is plain if it has not attribute or properties
     *
     * @param type
     * @return
     */
    private boolean isPlainElement(SchemaType type)
    {
        return (type.getAttributeProperties() == null || type.getAttributeProperties().length == 0)
               && (type.getElementProperties() == null || type.getElementProperties().length == 0);
    }

    private static SchemaType getSimpleBaseType(SchemaType type)
    {
        SchemaType basic = type;
        while (basic.getBaseType() != null && !basic.isSimpleType())
        {
            basic = basic.getBaseType();
        }

        return basic;
    }


    public QName getRootElementName()
    {
        return rootElementName;
    }

    public SchemaProvider getSchemas()
    {
        return schemas;
    }
}
