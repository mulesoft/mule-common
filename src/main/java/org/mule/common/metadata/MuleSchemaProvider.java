/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.common.metadata;


import org.mule.common.metadata.util.XmlSchemaUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.impl.common.HttpRetriever;


/**
 * A Provider that uses a custom retriever in case an HTTP/HTTPS request is needed
 */
public class MuleSchemaProvider implements SchemaProvider
{

    private List<String> schemas;
    private Charset encoding;
    private URL baseUri;
    private HttpRetriever retriever;

    public MuleSchemaProvider(List<String> schemas, Charset encoding, URL baseUri, HttpRetriever retriever)
    {
        this.schemas = schemas;
        this.encoding = encoding;
        this.baseUri = baseUri;
        this.retriever = retriever;
    }

    @Override
    public List<InputStream> getSchemas()
    {
        List<InputStream> result = new ArrayList<InputStream>();
        for (String schema : schemas)
        {
            result.add(new ByteArrayInputStream(schema.getBytes(encoding)));
        }
        return result;
    }

    @Override
    public SchemaGlobalElement findRootElement(QName rootElementName) throws XmlException
    {
        final SchemaTypeSystem schemaTypeLoader = XmlSchemaUtils.getSchemaTypeSystem(schemas, baseUri, retriever);
        return schemaTypeLoader.findElement(rootElementName);
    }

    @Override
    public SchemaType findRootType(QName rootElementName) throws XmlException
    {
        final SchemaTypeSystem schemaTypeLoader = XmlSchemaUtils.getSchemaTypeSystem(schemas, baseUri, retriever);
        return schemaTypeLoader.findType(rootElementName);
    }

}
