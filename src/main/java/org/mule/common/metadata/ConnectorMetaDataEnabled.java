package org.mule.common.metadata;

import java.util.List;

import org.mule.common.Capability;
import org.mule.common.Result;

public interface ConnectorMetaDataEnabled extends Capability
{
    /**
     * Get "all" of the MetaDataKeys of the MetaData that this connector works with (either inputs or outputs).
     * Obviously, this can't be all possible MetaData because sometimes they accept arbitrary input.
     * @return all (a best effort) MetaData that this connector works with. 
     */
    public Result<List<MetaDataKey>> getMetaDataKeys();

    public Result<MetaData> getMetaData(MetaDataKey key);
}
