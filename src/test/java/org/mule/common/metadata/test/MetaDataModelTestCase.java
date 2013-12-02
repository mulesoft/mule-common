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

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.mule.common.metadata.DefaultListMetaDataModel;
import org.mule.common.metadata.DefaultParameterizedMapMetaDataModel;
import org.mule.common.metadata.DefaultPojoMetaDataModel;
import org.mule.common.metadata.DefaultSimpleMetaDataModel;
import org.mule.common.metadata.ListMetaDataModel;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.datatype.DataType;

import org.junit.Test;

public class MetaDataModelTestCase
{

    @Test(expected = IllegalArgumentException.class)
    public void whenPojoUsedAsTypeForSimpleMetadataModelShouldFail()
    {
        new DefaultSimpleMetaDataModel(DataType.POJO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenListUsedAsTypeForSimpleMetadataModelShouldFail()
    {
        new DefaultSimpleMetaDataModel(DataType.LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenMapUsedAsTypeForSimpleMetadataModelShouldFail()
    {
        new DefaultSimpleMetaDataModel(DataType.MAP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenXmlUsedAsTypeForSimpleMetadataModelShouldFail()
    {
        new DefaultSimpleMetaDataModel(DataType.XML);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCsvUsedAsTypeForSimpleMetadataModelShouldFail()
    {
        new DefaultSimpleMetaDataModel(DataType.CSV);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenJsonUsedAsTypeForSimpleMetadataModelShouldFail()
    {
        new DefaultSimpleMetaDataModel(DataType.JSON);
    }

    @Test()
    public void whenStringUsedAsTypeForSimpleMetadataModelItShouldBeReturned()
    {
        DefaultSimpleMetaDataModel defaultSimpleMetaDataModel = new DefaultSimpleMetaDataModel(DataType.STRING);
        Assert.assertThat(defaultSimpleMetaDataModel.getDataType(), CoreMatchers.is(DataType.STRING));
    }

    @Test()
    public void testPojoMetadataModelReturnClassName()
    {
        DefaultPojoMetaDataModel defaultPojoMetaDataModel = new DefaultPojoMetaDataModel(DataType.class);
        Assert.assertThat(defaultPojoMetaDataModel.getClassName(), CoreMatchers.is(DataType.class.getName()));
        Assert.assertThat(defaultPojoMetaDataModel.isInterface(), CoreMatchers.is(false));
    }

    @Test()
    public void testListMetadataModelReturnClassName()
    {
        MetaDataModel elementModel = new DefaultPojoMetaDataModel(DataType.class);
        ListMetaDataModel defaultListMetaDataModel = new DefaultListMetaDataModel(elementModel);
        Assert.assertThat(defaultListMetaDataModel.getElementModel(), CoreMatchers.is(elementModel));
    }

    @Test()
    public void testParametrizedMapMetadataModelReturnClassName()
    {
        MetaDataModel key = new DefaultPojoMetaDataModel(DataType.class);
        MetaDataModel value = new DefaultPojoMetaDataModel(DataType.class);
        DefaultParameterizedMapMetaDataModel defaultParameterizedMapMetaDataModel = new DefaultParameterizedMapMetaDataModel(key, value);
        Assert.assertThat(defaultParameterizedMapMetaDataModel.getKeyMetaDataModel(), CoreMatchers.is(key));
        Assert.assertThat(defaultParameterizedMapMetaDataModel.getValueMetaDataModel(), CoreMatchers.is(value));
    }
}
