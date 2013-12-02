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

import org.mule.common.FailureType;

public class MetaDataFailureType extends FailureType
{
    public static final MetaDataFailureType ERROR_METADATA_KEYS_RETRIEVER = new MetaDataFailureType("ERROR_METADATA_KEYS_RETRIEVER");
    public static final MetaDataFailureType ERROR_METADATA_RETRIEVER = new MetaDataFailureType("ERROR_METADATA_RETRIEVER");

    protected MetaDataFailureType(String name)
    {
        super(name);
    }
}
