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

import org.mule.common.metadata.field.property.MetaDataFieldProperty;

import java.util.*;

/**
 * Default implementation for encapsulate the {@link MetaDataModel}.
 */
public class DefaultMetaData implements MetaData
{

    private MetaDataModel payload;
    private Map<MetaDataPropertyScope, MetaDataProperties> properties;


    /**
     * Default Constructor. Should be used on most use cases.
     */
    public DefaultMetaData(MetaDataModel payload)
    {
        this.payload = payload;
        this.properties = new TreeMap<MetaDataPropertyScope, MetaDataProperties>();
        initProperties();
    }

    /**
     * Copy constructor. Shouldn't use this use default instead.
     */
    public DefaultMetaData(MetaData oldMetadata, MetaDataModel payload)
    {
        this(payload);
        for (MetaDataPropertyScope value : MetaDataPropertyScope.values())
        {
            copyAllPropertiesWithScope(oldMetadata, value);
        }

    }

    public void copyAllPropertiesWithScope(MetaData oldMetadata, MetaDataPropertyScope propertyScope)
    {
        doGetProperties(propertyScope).addAll(oldMetadata.getProperties(propertyScope));
    }

    private void initProperties()
    {
        MetaDataPropertyScope[] values = MetaDataPropertyScope.values();
        for (MetaDataPropertyScope value : values)
        {
            getProperties().put(value, new MetaDataProperties());
        }
    }

    @Override
    public MetaDataModel getPayload()
    {
        return payload;
    }

    @Override
    public MetaDataProperties getProperties(MetaDataPropertyScope scope)
    {
        return doGetProperties(scope);
    }

    @Override
    public void addProperty(MetaDataPropertyScope scope, String name, MetaDataModel propertyModel, MetaDataFieldProperty... properties)
    {

        doGetProperties(scope).add(new DefaultMetaDataField(name, propertyModel, Arrays.asList(properties)));
    }

    @Override
    public void removeProperty(MetaDataPropertyScope scope, String name)
    {

        doGetProperties(scope).removeFieldByName(name);
    }

    private MetaDataProperties doGetProperties(MetaDataPropertyScope scope)
    {
        MetaDataProperties metaDataProperties = this.getProperties().get(scope);
        if (metaDataProperties == null) //this should only happen when loading old serialized objects
        {
            metaDataProperties = new MetaDataProperties();
            this.getProperties().put(scope, metaDataProperties);
        }
        return metaDataProperties;
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
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof DefaultMetaData))
        {
            return false;
        }
        DefaultMetaData other = (DefaultMetaData) obj;
        if (payload == null)
        {
            if (other.payload != null)
            {
                return false;
            }
        }
        else if (!payload.equals(other.payload))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "DefaultMetaData: { payload: " + ((payload != null) ? payload.toString() : "null") + " }";
    }

    private Map<MetaDataPropertyScope, MetaDataProperties> getProperties()
    {
        if (properties == null)
        {
            initProperties();
        }
        return properties;
    }
}


