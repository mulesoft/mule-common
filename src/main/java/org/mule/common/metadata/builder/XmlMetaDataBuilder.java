package org.mule.common.metadata.builder;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.mule.common.metadata.XmlMetaDataModel;

public interface XmlMetaDataBuilder<P extends MetaDataBuilder<?>> extends MetaDataBuilder<XmlMetaDataModel> {
    
    public DefaultXmlMetaDataBuilder<P> addSchemaStringList(String... schemas);
    
    public DefaultXmlMetaDataBuilder<P> addSchemaStreamList(InputStream... schemaStreams);
    
    public DefaultXmlMetaDataBuilder<P> setEncoding(Charset xmlCharset);
    
    public DefaultXmlMetaDataBuilder<P> setExample(String xmlExample);
}
