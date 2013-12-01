/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.common.metadata.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.mule.common.metadata.DefaultSimpleMetaDataModel;
import org.mule.common.metadata.SimpleMetaDataModel;
import org.mule.common.metadata.datatype.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class DefaultSimpleMetaDataModelTestCase
{
    public static final List<DataType> simpleDataTypes = Collections.unmodifiableList(Arrays.asList(
        new DataType[] { DataType.BOOLEAN, DataType.BYTE, DataType.DATE, DataType.DATE_TIME,
        DataType.ENUM, DataType.NUMBER, DataType.STREAM, DataType.STRING, DataType.VOID, DataType.DATE_TIME, DataType.LONG,
        DataType.INTEGER, DataType.DOUBLE, DataType.DECIMAL}));
    
    public static final List<DataType> complexDataTypes;
    static
    {
        List<DataType> complexTypes = new ArrayList<DataType>(Arrays.asList(DataType.values()));
        complexTypes.removeAll(simpleDataTypes);
        complexDataTypes = Collections.unmodifiableList(complexTypes); 
    }

    @Test
    public void testBasicDataTypes()
    {
        for (DataType dt : simpleDataTypes)
        {
            SimpleMetaDataModel smdm = new DefaultSimpleMetaDataModel(dt);
            assertEquals(dt, smdm.getDataType());
            assertSame(dt, smdm.getDataType());
        }
    }
    
    @Test
    public void testComplexDataTypes()
    {
        for (DataType dt : complexDataTypes)
        {
            try
            {
                new DefaultSimpleMetaDataModel(dt);
                fail("Should not be able to create a SimpleMetaDataModel with a complex type: " + dt.name());
            }
            catch (IllegalArgumentException e)
            {
                // expected
            }
        }
    }
}
