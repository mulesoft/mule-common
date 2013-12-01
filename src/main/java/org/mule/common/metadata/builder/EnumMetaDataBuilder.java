package org.mule.common.metadata.builder;

import java.util.List;

public interface EnumMetaDataBuilder<P extends MetaDataBuilder<?>> extends AddingOperatorsMetaDataFieldBuilder<P> {

    EnumMetaDataBuilder<P> setValues(String...strings);
    
    EnumMetaDataBuilder<P> setValues(List<String> values);

}
