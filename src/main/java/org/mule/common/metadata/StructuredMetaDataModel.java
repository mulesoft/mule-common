package org.mule.common.metadata;

import java.util.List;

/**
 * Represents meta data model that its structured can be described with a list of fields.
 */
public interface StructuredMetaDataModel extends MetaDataModel
{
    List<MetaDataField> getFields();
}
