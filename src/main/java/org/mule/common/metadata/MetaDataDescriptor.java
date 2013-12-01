package org.mule.common.metadata;

import org.mule.common.Result;

public interface MetaDataDescriptor
{
    public Result<MetaData> getInputMetaData();
    public Result<MetaData> getOutputMetaData(MetaData inputMetaData);
}
