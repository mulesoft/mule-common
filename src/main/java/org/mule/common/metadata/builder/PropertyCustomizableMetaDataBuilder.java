package org.mule.common.metadata.builder;


public interface PropertyCustomizableMetaDataBuilder<P extends MetaDataBuilder<?>> extends DynamicObjectFieldBuilder<P> {

    PropertyCustomizableMetaDataBuilder<P> isSelectCapable(boolean capable);

    PropertyCustomizableMetaDataBuilder<P> isOrderByCapable(boolean capable);

    CustomizingWhereMetaDataFieldBuilder<P> isWhereCapable(boolean capable);

    PropertyCustomizableMetaDataBuilder<P> setExample(String example);
}