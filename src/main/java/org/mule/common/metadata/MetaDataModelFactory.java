/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.common.metadata;

import org.mule.common.metadata.MetaDataField.FieldAccessType;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.datatype.DataTypeFactory;
import org.mule.common.metadata.field.property.FieldPropertyFactory;
import org.mule.common.metadata.field.property.MetaDataFieldProperty;
import org.mule.common.metadata.util.TypeResolver;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetaDataModelFactory
{
    private static MetaDataModelFactory instance = new MetaDataModelFactory();

    private DataTypeFactory factory;

    private MetaDataModelFactory()
    {
        factory = DataTypeFactory.getInstance();
    }

    public static MetaDataModelFactory getInstance()
    {
        return instance;
    }

    public List<MetaDataField> getFieldsForClass(Class<?> clazz, FieldPropertyFactory featureFactory)
    {
        return getFieldsForClass(clazz, new ParsingContext(), featureFactory);
    }

    public List<MetaDataField> getFieldsForClass(Class<?> clazz, ParsingContext context, FieldPropertyFactory featureFactory)
    {
        ArrayList<MetaDataField> result = new ArrayList<MetaDataField>();
        parseFields(clazz, context, featureFactory, result);
        return result;
    }

    /**
     * Parses given type and answers schema object corresponding to that type.
     */
    protected MetaDataModel parseType(Type type, ParsingContext context, FieldPropertyFactory featureFactory) {

        if (type instanceof Class<?>) {
            return parseClass((Class<?>)type, context, featureFactory);
        }
        if (type instanceof ParameterizedType) {
            /*
             * for parameterized type, discover raw type and resolved variables
             */
            ParameterizedType paramType = (ParameterizedType)type;

            Class<?> raw = TypeResolver.erase(type);

            if (Collection.class.isAssignableFrom(raw)) {                
                return  new DefaultListMetaDataModel(parseType(paramType.getActualTypeArguments()[0], context, featureFactory));
            }
            if (Map.class.isAssignableFrom(raw)) {                
                return new DefaultParameterizedMapMetaDataModel(parseType(paramType.getActualTypeArguments()[0], context, featureFactory), parseType(paramType.getActualTypeArguments()[1], context, featureFactory));
            }
            /*
             * some other parameterized type, resolve variables and proceed
             * with raw type
             */
            context.addResolvedVariables(TypeResolver.resolveVariables(type));

            return parseClass(raw, context, featureFactory);
        }
        if (type instanceof GenericArrayType) {            
            GenericArrayType arrayType = (GenericArrayType)type;
            return new DefaultListMetaDataModel(parseType(arrayType.getGenericComponentType(), context, featureFactory));
        }
        if (type instanceof TypeVariable<?>) {
            
            Type actual = context.getResolvedVariables().get(type);
            if (actual == null || type.equals(actual)) {
                
                TypeVariable<?> typeVariable = (TypeVariable<?>)type;
                Type bounds[] = typeVariable.getBounds();
                if (bounds.length > 0) {
                    return parseType(bounds[0], context, featureFactory);
                } else {
                    return context.OBJECT;
                }
            } else {
                return parseType(actual, context, featureFactory);
            }
        }
        if (type instanceof WildcardType) {
            
            WildcardType wildType = (WildcardType)type;
            Type lowerBounds[] = wildType.getLowerBounds();
            Type upperBounds[] = wildType.getUpperBounds();
            
            if (lowerBounds.length > 0) {
                return parseType(lowerBounds[0], context, featureFactory);
            }
            if (upperBounds.length > 0) {
                return parseType(upperBounds[0], context, featureFactory);
            }
            return context.OBJECT;
        }
        throw new IllegalArgumentException("Unsupported type " + type);
    }

    protected MetaDataModel parseClass(Class<?> klass, ParsingContext context, FieldPropertyFactory featureFactory) {

        if (Collection.class.isAssignableFrom(klass)) {
            /*
             * find declaration that caused this class to be a Collection,
             * we want type arguments from it
             */
            Type declaring = TypeResolver.getGenericSuperclass(klass, Collection.class);
            if (declaring == null) {
                /*
                 * no generic parent found, proceed on super type
                 */
                declaring = TypeResolver.getSuperclass(klass, Collection.class);
            }
            if (declaring != null) {
                return  parseType(declaring, context, featureFactory);
            }
            /*
             * we are directly on interface
             */
            return  new  DefaultListMetaDataModel(context.OBJECT);            
        }
        if (klass.isArray()) {            
            return new DefaultListMetaDataModel(parseClass(klass.getComponentType(), context, featureFactory), true);
        }
        if (Map.class.isAssignableFrom(klass)) {
            /*
             * find declaration that caused this class to be a Map,
             * we want type arguments from it
             */
            Type declaring = TypeResolver.getGenericSuperclass(klass, Map.class);
            if (declaring == null) {
                /*
                 * no parameterized super type found, so proceed to parent
                 */
                declaring = TypeResolver.getSuperclass(klass, Map.class);
            }
            if (declaring != null) {
                return parseType(declaring, context, featureFactory);
            }
            
            /*
             * we are directly on raw interface
             */
            return new DefaultParameterizedMapMetaDataModel(context.OBJECT, context.OBJECT);            
        }

        DataType dataType = factory.getDataType(klass);
        if (dataType == DataType.POJO){
            return parseBeanType(klass, context, featureFactory);
        }else{
            return new DefaultSimpleMetaDataModel(dataType);
        }
        
    }

    protected static class ParsingContext {

        private Map<TypeVariable<?>, Type> resolvedVariables = new HashMap<TypeVariable<?>, Type>();

        public final MetaDataModel OBJECT = new DefaultPojoMetaDataModel(Object.class, new ArrayList<MetaDataField>());
        private final Map<String, MetaDataModel> typedObjects = new HashMap<String, MetaDataModel>();
        {
            addTypedObject(Object.class.getName(), OBJECT);
        }

        public MetaDataModel getTypedObject(String typeName) {
            return typedObjects.get(typeName);
        }

        public void addTypedObject(String typeName,MetaDataModel typedObject) {
            if (!typedObjects.containsKey(typeName)) {
                typedObjects.put(typeName, typedObject);
            }
        }

        /**
         * @return the resolvedVariables
         */
        public Map<TypeVariable<?>, Type> getResolvedVariables() {
            return Collections.unmodifiableMap(resolvedVariables);
        }

        public void addResolvedVariables(Map<TypeVariable<?>, Type> map) {

            for (Map.Entry<TypeVariable<?>, Type> entry : map.entrySet()) {
                if (resolvedVariables.containsKey(entry.getKey())) {
                    /*
                     * only replace if value is something usable
                     */
                    if (!(entry.getValue() instanceof TypeVariable<?>)) {
                        resolvedVariables.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    resolvedVariables.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public static List<Field> getInheritedPrivateFields(Class<?> type) {
        List<Field> result = new ArrayList<Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            for (Field field : i.getDeclaredFields()) {
                if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
                    result.add(field);
                }
            }
            i = i.getSuperclass();
        }

        return result;
    }

    public Set<String> getParentNames(Class<?> clazz)
    {
        Set<String> parents = new HashSet<String>();
        for (Class<?> c : clazz.getInterfaces())
        {
            if (c != null)
            {
                parents.add(c.getName());
                parents.addAll(getParentNames(c));
            }
        }
        Class<?> parent = clazz.getSuperclass();
        if (parent != null)
        {
            parents.add(parent.getName());
            parents.addAll(getParentNames(parent));
        }
        return parents;
    }

    protected MetaDataModel parseBeanType(Class<?> klass, ParsingContext context, FieldPropertyFactory featureFactory) {

        if(context.getTypedObject(klass.getName())!= null){
            return context.getTypedObject(klass.getName());
        }
        ArrayList<MetaDataField> fields = new ArrayList<MetaDataField>();
        DefaultPojoMetaDataModel typedObject = new DefaultPojoMetaDataModel(klass,fields);
        context.addTypedObject(klass.getName(),typedObject);
        /*
         * resolve type variables in the class
         */
        context.addResolvedVariables(TypeResolver.resolveVariables(klass));
        /*
         * parse properties
         */
        parseFields(klass, context, featureFactory, fields);
        
        return typedObject;
    }

    private void parseFields(Class<?> klass, ParsingContext context, FieldPropertyFactory featureFactory, ArrayList<MetaDataField> fields) {
        if (!klass.equals(Object.class)) {
            try {
                List<PropertyDescriptor> propertyDescriptors = null;
                if (klass.isInterface()) {
                    /*
                     * collect properties from whole hierarchy
                     */
                    propertyDescriptors = new ArrayList<PropertyDescriptor>();
                    propertyDescriptors.addAll(Arrays.asList(Introspector.getBeanInfo(klass).getPropertyDescriptors()));
                    for (Class<?> interfaceType : TypeResolver.getSuperInterfaces(klass)) {
                        propertyDescriptors.addAll(Arrays.asList(
                                Introspector.getBeanInfo(interfaceType).getPropertyDescriptors()));
                    }
                } else {
                    propertyDescriptors = Arrays.asList(Introspector.getBeanInfo(klass, Object.class).getPropertyDescriptors());
                }

                for (PropertyDescriptor pd : propertyDescriptors) {
                    final Class<?> propertyClass = pd.getPropertyType();
                    if (propertyClass != null) {    // property class returns null if there are getters with no associated field.
                        final List<MetaDataFieldProperty> classRelatedProperties = featureFactory.getProperties(propertyClass);

                        if (pd.getReadMethod() != null && pd.getWriteMethod() != null) {
                            Type propertyType = pd.getReadMethod().getGenericReturnType();
                            MetaDataModel property = parseType(propertyType, context, featureFactory);

                            final List<MetaDataFieldProperty> properties = featureFactory.getProperties(pd.getName(), property);
                            properties.addAll(classRelatedProperties);

                            fields.add(new DefaultMetaDataField(pd.getName(), property, properties));
                        } else if (pd.getReadMethod() != null) {
                            Type propertyType = pd.getReadMethod().getGenericReturnType();
                            MetaDataModel property = parseType(propertyType, context, featureFactory);

                            final List<MetaDataFieldProperty> properties = featureFactory.getProperties(pd.getName(), property);
                            properties.addAll(classRelatedProperties);

                            fields.add(new DefaultMetaDataField(pd.getName(), property, FieldAccessType.READ, properties));
                        } else if (pd.getWriteMethod() != null) {
                            Type propertyType = pd.getWriteMethod().getGenericReturnType();
                            MetaDataModel property = parseType(propertyType, context, featureFactory);

                            final List<MetaDataFieldProperty> properties = featureFactory.getProperties(pd.getName(), property);
                            properties.addAll(classRelatedProperties);

                            fields.add(new DefaultMetaDataField(pd.getName(), property, FieldAccessType.WRITE, properties));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}


