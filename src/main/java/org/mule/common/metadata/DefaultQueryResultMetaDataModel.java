package org.mule.common.metadata;

import java.util.List;
import java.util.Set;

import org.mule.common.metadata.datatype.DataType;

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

    public String getName()
    {
        return definedMapMetaDataModel.getName();
    }

    public Set<String> getKeys() {
        return definedMapMetaDataModel.getKeys();
    }

    public MetaDataModel getKeyMetaDataModel()
    {
        return definedMapMetaDataModel.getKeyMetaDataModel();
    }

    public MetaDataModel getValueMetaDataModel(String key)
    {
        return definedMapMetaDataModel.getValueMetaDataModel(key);
    }

    public List<MetaDataField> getFields() {
        return definedMapMetaDataModel.getFields();
    }

    public DataType getDataType()
    {
        return definedMapMetaDataModel.getDataType();
    }

    public <T extends MetaDataModel> T as(Class<T> clazz) {
        if ((clazz.isAssignableFrom(this.getClass())))
        {
            return clazz.cast(this);
        }
        return null;
    }

    public void accept(MetaDataModelVisitor modelVisitor)
    {
        modelVisitor.visitDynamicMapModel(this);
    }

    public String getImplementationClass() {
        return definedMapMetaDataModel.getImplementationClass();
    }

}
