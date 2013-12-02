package org.mule.common.metadata;

import java.io.InputStream;
import java.util.List;

/**
 * Represents an XML Object.
 */
public interface XmlMetaDataModel extends MetaDataModel
{
    List<InputStream> getSchemas();

    String getExample();

    void setExample(String xmlExample);

    String getRootElement();
}
