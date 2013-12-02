/**
 *
 */
package org.mule.common.metadata.builder;

import org.mule.common.metadata.DefaultPojoMetaDataModel;
import org.mule.common.metadata.PojoMetaDataModel;
import org.mule.common.metadata.field.property.DefaultFieldPropertyFactory;
import org.mule.common.metadata.field.property.FieldPropertyFactory;

public class DefaultPojoMetaDataBuilder<P extends MetaDataBuilder<?>> implements PojoMetaDataBuilder<P>
{

    private Class<?> pojoClass;
    private FieldPropertyFactory factory;
    private P parentBuilder;

    DefaultPojoMetaDataBuilder(Class<?> pojoClass,P parentBuilder)
    {
        this.pojoClass = pojoClass;
        this.parentBuilder = parentBuilder;
    }

    public PojoMetaDataModel build()
    {
        if (factory == null)
        {
            factory = new DefaultFieldPropertyFactory();
        }
        return new DefaultPojoMetaDataModel(pojoClass, factory);
    }

    public PojoMetaDataBuilder<P> usingFieldPropertyFactory(FieldPropertyFactory factory)
    {
        this.factory = factory;
        return this;
    }

    public P endPojo()
    {
        return parentBuilder;
    }
}
