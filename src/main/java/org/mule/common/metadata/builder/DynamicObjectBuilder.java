/**
 *
 */
package org.mule.common.metadata.builder;


import org.mule.common.metadata.DefinedMapMetaDataModel;
import org.mule.common.metadata.datatype.DataType;

public interface DynamicObjectBuilder<P extends MetaDataBuilder<?>> extends MetaDataBuilder<DefinedMapMetaDataModel> {

    PropertyCustomizableMetaDataBuilder<P> addSimpleField(String name, DataType dataType);

    PropertyCustomizableMetaDataBuilder<P> addSimpleField(String name, DataType dataType, String implClass);

    EnumMetaDataBuilder<P> addEnumField(String name);

    EnumMetaDataBuilder<P> addEnumField(String name,  String implClass);

    DynamicObjectFieldBuilder<P> addPojoField(String name, Class<?> pojo);

    @Deprecated
    DynamicObjectFieldBuilder<DynamicObjectFieldBuilder<P>> addListOfDynamicObjectField(String name);

    ListMetaDataBuilder<DynamicObjectFieldBuilder<P>> addList(String name);

    DynamicObjectFieldBuilder<DynamicObjectFieldBuilder<P>> addDynamicObjectField(String name);

    P endDynamicObject();
}
