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

public class DefaultMetaData implements MetaData
{

    private MetaDataModel payload;

    public DefaultMetaData(MetaDataModel payload)
    {
        this.payload = payload;
    }

    public MetaDataModel getPayload()
    {
        return payload;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((payload == null) ? 0 : payload.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof DefaultMetaData)) return false;
        DefaultMetaData other = (DefaultMetaData) obj;
        if (payload == null)
        {
            if (other.payload != null) return false;
        }
        else if (!payload.equals(other.payload)) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "DefaultMetaData: { payload: " + ((payload != null) ? payload.toString() : "null") + " }";
    }
}
