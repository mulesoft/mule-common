package org.mule.common.metadata;

public interface ListMetaDataModel extends MetaDataModel
{
    public MetaDataModel getElementModel();

    public boolean isArray();
}
