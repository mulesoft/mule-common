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

public class DefaultSimpleMetaDataModel 
	extends AbstractMetaDataModel implements SimpleMetaDataModel
{
    /**
     * Used for Define Simple Types
     * @param dataType
     */
    public DefaultSimpleMetaDataModel(DataType dataType)
    {
        super(dataType);
        if (dataType == DataType.POJO || dataType == DataType.MAP || dataType == DataType.LIST || dataType == DataType.XML || dataType == DataType.CSV || dataType == DataType.JSON)
        {
            throw new IllegalArgumentException("Invalid DataType for SimpleMetadataModel " + dataType);
        }
    }

    @Override
    public String toString()
    {
        return "DefaultSimpleMetaDataModel:{ dataType:" + getDataType() != null ? getDataType().toString() : "null" + " }";
    }

    public void accept(MetaDataModelVisitor modelVisitor) {
        modelVisitor.visitSimpleMetaDataModel(this);
    }

}
