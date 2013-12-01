package org.mule.common.metadata.builder;

import org.mule.common.metadata.MetaDataModel;

public class DefaultMetaDataBuilder implements MetaDataBuilder<MetaDataModel>
{
    private MetaDataBuilder<MetaDataModel> root;

    public ListMetaDataBuilder<?> createList()
    {
        final DefaultListMetaDataBuilder result = new DefaultListMetaDataBuilder(this);
        root = result;
        return result;
    }

    public PojoMetaDataBuilder<?> createPojo(Class<?> pojo)
    {
        final DefaultPojoMetaDataBuilder result = new DefaultPojoMetaDataBuilder(pojo, this);
        root = result;
        return result;
    }

    public DynamicObjectBuilder<?> createDynamicObject(String name)
    {
        DefaultDynamicObjectBuilder result = new DefaultDynamicObjectBuilder(name, this);
        root = result;
        return result;
    }

    public XmlMetaDataBuilder createXmlObject(String name)
    {
        DefaultXmlMetaDataBuilder result = new DefaultXmlMetaDataBuilder(name);
        root = result;
        return result;
    }    

    public MetaDataModel build()
    {
        return root.build();
    }
}
