/**
 *
 */
package org.mule.common.metadata.builder;

import org.mule.common.metadata.SimpleMetaDataModel;

public interface SimpleMetaDataBuilder<P extends MetaDataBuilder<?>> extends MetaDataBuilder<SimpleMetaDataModel> {

    P endSimpleField();
}
