package org.mule.common.metadata.builder;

public interface AddingOperatorsMetaDataFieldBuilder<P extends MetaDataBuilder<?>> extends CustomizingWhereMetaDataFieldBuilder<P> {

    AddingOperatorsMetaDataFieldBuilder<P> supportsEquals();

    AddingOperatorsMetaDataFieldBuilder<P> supportsNotEquals();

    AddingOperatorsMetaDataFieldBuilder<P> supportsGreater();

    AddingOperatorsMetaDataFieldBuilder<P> supportsGreaterOrEquals();

    AddingOperatorsMetaDataFieldBuilder<P> supportsLess();

    AddingOperatorsMetaDataFieldBuilder<P> supportsLessOrEquals();

    AddingOperatorsMetaDataFieldBuilder<P> supportsLike();
}
