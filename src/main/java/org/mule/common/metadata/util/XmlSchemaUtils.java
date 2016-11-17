package org.mule.common.metadata.util;

import org.mule.common.metadata.XmlMetaDataFieldFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler;


public class XmlSchemaUtils {

    public static SchemaTypeSystem getSchemaTypeSystem(List<String> schemas) throws XmlException {
        return getSchemaTypeSystem(schemas, null);
    }

    public static SchemaTypeSystem getSchemaTypeSystem(List<String> schemas, URL source) throws XmlException {
        final XmlOptions options = new XmlOptions();
        options.setCompileNoUpaRule();
        options.setCompileNoValidation();
        options.setCompileDownloadUrls();
        if (source != null) {
            options.put(XmlOptions.DOCUMENT_SOURCE_NAME, source.toString());
            try
            {
                options.put("BASE_URI", new URI(PathUtilities.extractDirectoryPath(source.toString())));
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
        }

        /* Load the schema */
        final XmlObject[] schemaRepresentation = new XmlObject[schemas.size()];
        final SchemaTypeLoader contextTypeLoader = SchemaTypeLoaderImpl.build(new SchemaTypeLoader[]{XmlBeans.getBuiltinTypeSystem()}, null, XmlMetaDataFieldFactory.class.getClassLoader());
        for (int i = 0; i < schemas.size(); i++) {
            schemaRepresentation[i] = contextTypeLoader.parse(schemas.get(i), null, options);
        }
        return SchemaTypeSystemCompiler.compile(null, null, schemaRepresentation, null, contextTypeLoader, null, options);
    }


    public static SchemaTypeSystem getSchemaTypeSystemFromUrl(List<URL> schemas) throws XmlException, IOException {
        final XmlOptions options = new XmlOptions();
        options.setCompileNoUpaRule();
        options.setCompileNoValidation();
        options.setCompileDownloadUrls();

        /* Load the schema */
        final XmlObject[] schemaRepresentation = new XmlObject[schemas.size()];
        final SchemaTypeLoader contextTypeLoader = SchemaTypeLoaderImpl.build(new SchemaTypeLoader[]{XmlBeans.getBuiltinTypeSystem()}, null,
                XmlMetaDataFieldFactory.class.getClassLoader());

        int i = 0;
        for (URL schemaURL : schemas) {
            XmlObject schemaObject = contextTypeLoader.parse(schemaURL, null, options);
            schemaRepresentation[i] = schemaObject;
            i++;
        }
        return SchemaTypeSystemCompiler.compile(null, null, schemaRepresentation, null, contextTypeLoader, null, options);
    }


}


