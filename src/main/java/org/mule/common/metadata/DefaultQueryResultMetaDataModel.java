package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;
import java.util.Set;

/**
 * Model for representing query metadata
 */

public class DefaultQueryResultMetaDataModel implements QueryResultMetaDataModel
{

    private DefinedMapMetaDataModel definedMapMetaDataModel;

    public DefaultQueryResultMetaDataModel(DefinedMapMetaDataModel definedMapMetaDataModel)
    {
        this.definedMapMetaDataModel = definedMapMetaDataModel;
    }

    @Override
    public String getName()
    {
        return definedMapMetaDataModel.getName();
    }

    @Override
    public Set<String> getKeys() {
        return definedMapMetaDataModel.getKeys();
    }

    @Override
    public MetaDataModel getKeyMetaDataModel()
    {
        return definedMapMetaDataModel.getKeyMetaDataModel();
    }

    @Override
    public MetaDataModel getValueMetaDataModel(String key)
    {
        return definedMapMetaDataModel.getValueMetaDataModel(key);
    }

    @Override
    public DataType getDataType()
    {
        return definedMapMetaDataModel.getDataType();
    }

    @Override
    public <T extends MetaDataModel> T as(Class<T> clazz)
    {
        return definedMapMetaDataModel.as(clazz);
    }

    @Override
    public void accept(MetaDataModelVisitor modelVisitor)
    {
        definedMapMetaDataModel.accept(modelVisitor);
    }
}