package org.mule.common.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.parser.json.*;

/**
 * Created by studio on 18/07/2014.
 */
public class JSONSchemaMetaDataFieldFactory implements MetaDataFieldFactory {

    private final JSONMetadataModelFactoryHelper helper;

    private JSONType jsonSchemaType;

    public JSONSchemaMetaDataFieldFactory(JSONObjectType type) {
        this(type, new JSONMetadataModelFactoryHelper());
    }

    public JSONSchemaMetaDataFieldFactory(JSONObjectType type, Map<JSONObjectType, DefaultStructuredMetadataModel> visitedTypes) {
        this(type, new JSONMetadataModelFactoryHelper(visitedTypes));
    }

    public JSONSchemaMetaDataFieldFactory(JSONObjectType type, JSONMetadataModelFactoryHelper helper) {
        jsonSchemaType = type;
        this.helper = helper;
    }

    public List<MetaDataField> createFields(){
        List<MetaDataField> metaDataFields = new ArrayList<MetaDataField>();
        loadFields((JSONObjectType) jsonSchemaType, metaDataFields);
        return metaDataFields;
    }

    private void processJSONSchemaElement(JSONType property, String name, List<MetaDataField> metadata)  {
        if (property.isJSONObject()) {
            processJSONSchemaObject((JSONObjectType) property, name, metadata);
        } else if (property.isJSONPrimitive()) {
            processJSONSchemaPrimitive(property, name, metadata);
        } else if (property.isJSONArray()) {
            processJSONSchemaArray((JSONArrayType) property, name, metadata);
        } else if(property.isJSONPointer()){
            processJSONPointer((JSONPointerType)property, name, metadata);
        }
    }

    private void processJSONSchemaObject(JSONObjectType type, String name, List<MetaDataField> metadata){

        DefaultStructuredMetadataModel model = helper.buildJSONMetaDataModel(type);
        metadata.add(new DefaultMetaDataField(name, model));

    }

    private void loadFields(JSONObjectType type, List<MetaDataField> metadata)  {
        String[] properties = type.getProperties();
        for (String key : properties) {
            JSONType propertyType = type.getPropertyType(key);
            processJSONSchemaElement(propertyType, key, metadata);
        }
    }

    private void processJSONSchemaArray(JSONArrayType property, String name, List<MetaDataField> metadata){
        AbstractMetaDataModel model = helper.buildJSONArrayMetaDataModel(property);
        metadata.add(new DefaultMetaDataField(name, new DefaultListMetaDataModel(model)));

    }

    private void processJSONSchemaPrimitive(JSONType property, String name, List<MetaDataField> metadata) {
        DataType dataType = JSONTypeUtils.getDataType(property);
        MetaDataModel model = dataType==DataType.UNKNOWN ? new DefaultUnknownMetaDataModel(): new DefaultSimpleMetaDataModel(dataType);
        metadata.add(new DefaultMetaDataField(name, model));
    }

    private void processJSONPointer(JSONPointerType ptr, String name, List<MetaDataField> metadata) {
        processJSONSchemaElement(ptr.resolve(), name, metadata);
    }

}
