/**
 *
 */
package org.mule.common.metadata.builder;

import org.mule.common.metadata.DefaultListMetaDataModel;
import org.mule.common.metadata.ListMetaDataModel;
import org.mule.common.metadata.datatype.DataType;

public class DefaultListMetaDataBuilder<P extends MetaDataBuilder<?>> implements ListMetaDataBuilder<P>
{

    private MetaDataBuilder innerBuilder;
    private P parentBuilder;

    DefaultListMetaDataBuilder(P parentBuilder)
    {
        this.parentBuilder = parentBuilder;
    }

    public PojoMetaDataBuilder<ListMetaDataBuilder<P>> ofPojo(Class<?> pojoClass)
    {
        innerBuilder = new DefaultPojoMetaDataBuilder<ListMetaDataBuilder<?>>(pojoClass,this);
        return (PojoMetaDataBuilder) innerBuilder;
    }

    public DynamicObjectBuilder<ListMetaDataBuilder<P>> ofDynamicObject(String name)
    {
        innerBuilder = new DefaultDynamicObjectBuilder<ListMetaDataBuilder<?>>(name,this);
        return (DynamicObjectBuilder) innerBuilder;
    }

    public ListMetaDataBuilder<ListMetaDataBuilder<P>> ofList()
    {
        innerBuilder = new DefaultListMetaDataBuilder<ListMetaDataBuilder<?>>(this);
        return (ListMetaDataBuilder) innerBuilder;
    }

    public P ofSimpleField(DataType dataType)
    {
        innerBuilder = new DefaultSimpleMetaDataBuilder<ListMetaDataBuilder<?>>(dataType, this);
        return endList();
    }

    public P endList()
    {
        return parentBuilder;
    }

    public ListMetaDataModel build()
    {
        return new DefaultListMetaDataModel(innerBuilder.build());
    }
}
