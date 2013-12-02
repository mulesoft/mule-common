package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;

public interface MetaDataModel {

    DataType getDataType();

    <T extends MetaDataModel> T as(Class<T> clazz);

    void accept(MetaDataModelVisitor modelVisitor);

    String getImplementationClass();
}
