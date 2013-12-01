package org.mule.common.metadata.util;

import org.mule.common.metadata.DefaultDefinedMapMetaDataModel;
import org.mule.common.metadata.DefaultListMetaDataModel;
import org.mule.common.metadata.DefaultPojoMetaDataModel;
import org.mule.common.metadata.DefaultSimpleMetaDataModel;
import org.mule.common.metadata.DefinedMapMetaDataModel;
import org.mule.common.metadata.ListMetaDataModel;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.MetaDataQueryFilterVisitor;
import org.mule.common.metadata.QueryResultMetaDataModel;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.query.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MetaDataFilterTest {
    @Test
      public void testFilter() throws Exception {

        Map<String,MetaDataModel> map = new HashMap<String, MetaDataModel>();

        map.put("filterThis", new DefaultSimpleMetaDataModel(DataType.STRING));
        map.put("noFilterThis", new DefaultPojoMetaDataModel(DataType.class));
        map.put("nietherThis", new DefaultSimpleMetaDataModel(DataType.NUMBER));
        map.put("thisIsOk", new DefaultPojoMetaDataModel(MetaDataFilterTest.class));
        DefaultDefinedMapMetaDataModel model = new DefaultDefinedMapMetaDataModel(map, "Account");

        List<Field> filters = new ArrayList<Field>();
        filters.add(new Field("filterThis","type"));
        filters.add(new Field("thisIsOk","otherType"));

        MetaDataQueryFilterVisitor visitor = new MetaDataQueryFilterVisitor(filters);

        model.accept(visitor);

        Assert.assertEquals(2,((DefinedMapMetaDataModel)visitor.filteringResult().getPayload()).getKeys().size());
    }

    @Test
    public void testQueryFilter() throws Exception {

        Map<String,MetaDataModel> map = new HashMap<String, MetaDataModel>();

        map.put("filterThis", new DefaultSimpleMetaDataModel(DataType.STRING));
        map.put("noFilterThis", new DefaultPojoMetaDataModel(DataType.class));
        map.put("nietherThis", new DefaultSimpleMetaDataModel(DataType.NUMBER));
        map.put("thisIsOk", new DefaultPojoMetaDataModel(MetaDataFilterTest.class));
        DefaultDefinedMapMetaDataModel model = new DefaultDefinedMapMetaDataModel(map, "Account");

        List<Field> filters = new ArrayList<Field>();
        filters.add(new Field("filterThis","type"));
        filters.add(new Field("thisIsOk","otherType"));

        MetaDataQueryFilterVisitor visitor = new MetaDataQueryFilterVisitor(filters);

        model.accept(visitor);

        Assert.assertEquals(2, visitor.filteringResult().getPayload().as(QueryResultMetaDataModel.class).getKeys().size());
    }

    @Test
    public void testFilterList() throws Exception {

        Map<String,MetaDataModel> map = new HashMap<String, MetaDataModel>();

        map.put("filterThis", new DefaultSimpleMetaDataModel(DataType.STRING));
        map.put("noFilterThis", new DefaultPojoMetaDataModel(DataType.class));
        map.put("nietherThis", new DefaultSimpleMetaDataModel(DataType.NUMBER));
        map.put("thisIsOk", new DefaultPojoMetaDataModel(MetaDataFilterTest.class));
        DefaultDefinedMapMetaDataModel model = new DefaultDefinedMapMetaDataModel(map, "Account");

        DefaultListMetaDataModel listModel = new DefaultListMetaDataModel(model);

        List<Field> filters = new ArrayList<Field>();
        filters.add(new Field("filterThis","type"));
        filters.add(new Field("thisIsOk","otherType"));

        MetaDataQueryFilterVisitor visitor = new MetaDataQueryFilterVisitor(filters);

        listModel.accept(visitor);

        Assert.assertEquals(2,visitor.filteringResult().getPayload().as(ListMetaDataModel.class).getElementModel().as(DefinedMapMetaDataModel.class).getKeys().size());
    }

    @Test
    public void testQueryFilterTest() throws Exception {

        Map<String,MetaDataModel> map = new HashMap<String, MetaDataModel>();

        map.put("filterThis", new DefaultSimpleMetaDataModel(DataType.STRING));
        map.put("noFilterThis", new DefaultPojoMetaDataModel(DataType.class));
        map.put("nietherThis", new DefaultSimpleMetaDataModel(DataType.NUMBER));
        map.put("thisIsOk", new DefaultPojoMetaDataModel(MetaDataFilterTest.class));
        DefaultDefinedMapMetaDataModel model = new DefaultDefinedMapMetaDataModel(map, "Account");

        DefaultListMetaDataModel listModel = new DefaultListMetaDataModel(model);

        List<Field> filters = new ArrayList<Field>();
        filters.add(new Field("filterThis","type"));
        filters.add(new Field("thisIsOk","otherType"));

        MetaDataQueryFilterVisitor visitor = new MetaDataQueryFilterVisitor(filters);

        listModel.accept(visitor);

        Assert.assertEquals(2,visitor.filteringResult().getPayload().as(ListMetaDataModel.class).getElementModel().as(QueryResultMetaDataModel.class).getKeys().size());
    }
}
