package org.mule.common.metadata.builder;


public interface CustomizingWhereMetaDataFieldBuilder<P extends MetaDataBuilder<?>> extends PropertyCustomizableMetaDataBuilder<P> {
    
    AddingOperatorsMetaDataFieldBuilder<P> withSpecificOperations();
    
    PropertyCustomizableMetaDataBuilder<P> withDefaultOperations();
}
