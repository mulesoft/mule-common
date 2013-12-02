package org.mule.common.metadata.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mule.common.metadata.DefaultXmlMetaDataModel;
import org.mule.common.metadata.XmlMetaDataModel;

public class DefaultXmlMetaDataBuilder<P extends MetaDataBuilder<?>> implements XmlMetaDataBuilder<P> {

    public String name;
    public String[] schemas;
    public InputStream[] schemasStream;
    public Charset encoding = Charset.forName("UTF-8");
    public String example;
    
    public DefaultXmlMetaDataBuilder(String name) {
        this.name = name;
    }

    public XmlMetaDataModel build() {
        XmlMetaDataModel model = null;
        if (schemas != null) {
            model = new DefaultXmlMetaDataModel(Arrays.asList(schemas), name, encoding);
        } else if (schemasStream != null) {

            List<String> result = new ArrayList<String>();
            for (InputStream schemaStream : schemasStream) {
                result.add(getStringFromInputStream(schemaStream, encoding));
            }

            model = new DefaultXmlMetaDataModel(result, name, encoding);
        }
        
        return model;
    }

    private static String getStringFromInputStream(InputStream is, Charset encoding) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is, encoding));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public DefaultXmlMetaDataBuilder<P> addSchemaStringList(String... schemas) {
        this.schemas = schemas;
        this.schemasStream = null;
        return this;
    }

    public DefaultXmlMetaDataBuilder<P> addSchemaStreamList(InputStream... schemaStreams) {
        this.schemasStream = schemaStreams;
        this.schemas = null;
        return this;
    }

    public DefaultXmlMetaDataBuilder<P> setEncoding(Charset encoding) {
        this.encoding = encoding;
        return this;
    }

    public DefaultXmlMetaDataBuilder<P> setExample(String xmlExample) {
        this.example = xmlExample;
        return this;
    }
}
