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

import org.mule.common.metadata.datatype.DataType;

public class DefaultParameterizedMapMetaDataModel extends AbstractMetaDataModel implements ParameterizedMapMetaDataModel
{
    private MetaDataModel keyMetaDataModel;
    private MetaDataModel valueMetaDataModel;
    private String name;

    public DefaultParameterizedMapMetaDataModel(MetaDataModel keyMetaDataModel, MetaDataModel valueMetaDataModel)
    {
        this(keyMetaDataModel, valueMetaDataModel, null);
    }

    public DefaultParameterizedMapMetaDataModel(MetaDataModel keyMetaDataModel, MetaDataModel valueMetaDataModel, String name)
    {
        super(DataType.MAP);
        this.keyMetaDataModel = keyMetaDataModel;
        this.valueMetaDataModel = valueMetaDataModel;
        this.name = name;
    }

    public MetaDataModel getKeyMetaDataModel()
    {
        return keyMetaDataModel;
    }

    public MetaDataModel getValueMetaDataModel()
    {
        return valueMetaDataModel;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((keyMetaDataModel == null) ? 0 : keyMetaDataModel.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((valueMetaDataModel == null) ? 0 : valueMetaDataModel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (!(obj instanceof DefaultParameterizedMapMetaDataModel))
        {
            return false;
        }
        DefaultParameterizedMapMetaDataModel other = (DefaultParameterizedMapMetaDataModel) obj;
        if (keyMetaDataModel == null)
        {
            if (other.keyMetaDataModel != null)
            {
                return false;
            }
        }
        else if (!keyMetaDataModel.equals(other.keyMetaDataModel))
        {
            return false;
        }
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (valueMetaDataModel == null)
        {
            if (other.valueMetaDataModel != null)
            {
                return false;
            }
        }
        else if (!valueMetaDataModel.equals(other.valueMetaDataModel))
        {
            return false;
        }
        return true;
    }


    public void accept(MetaDataModelVisitor modelVisitor) {
        modelVisitor.visitStaticMapModel(this);
    }

}
