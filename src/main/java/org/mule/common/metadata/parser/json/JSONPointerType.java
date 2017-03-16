package org.mule.common.metadata.parser.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class JSONPointerType implements JSONType
{

    public static final String HASH = "#";
    private java.lang.String ref;
    private SchemaEnv env;

    public JSONPointerType(SchemaEnv env, java.lang.String reference)
    {
        this.ref = reference;
        this.env = env;
    }

    public JSONType resolve()
    {

        if (HASH.equals(ref))
        {
            return env.lookupType(HASH);
        }

        JSONType referenceType = null;
        String baseURI = "";
        String jsonPointer = null;
        if (ref.contains(HASH))
        {
            String[] splitRef = ref.split("#");
            switch (splitRef.length)
            {
                case 1:
                    baseURI = splitRef[0];
                    jsonPointer = "#";
                    break;
                case 2:
                    baseURI = splitRef[0];
                    jsonPointer = splitRef[1];
                    break;
            }
        }
        else
        {
            baseURI = ref;
            jsonPointer = HASH;
        }

        SchemaEnv environment = env;
        if (!baseURI.isEmpty())
        {
            JSONObject remoteSchema;
            URL url;
            try
            {
                url = new URL(baseURI);
                remoteSchema = getRemoteSchema(url);
                environment = createSchemaEnv(env, remoteSchema);
            }
            catch (MalformedURLException e)
            {
                //Try to get schema from a file in relative path
                URL contextJsonURL = env.getContextJsonURL();
                URL urlFile;
                try
                {
                    urlFile = new URL(contextJsonURL, baseURI);
                    remoteSchema = getRemoteSchema(urlFile);
                    environment = createSchemaEnv(env, remoteSchema);
                }
                catch (MalformedURLException e1)
                {
                    throw new SchemaException(e1);
                }

            }
        }

        // See if it has already been resolved if base URI is empty or matches this schema's id
        JSONObject contextJsonObject = environment.getContextJsonObject();
        
        if (baseURI.equals("") || (contextJsonObject.has("id") && baseURI.equals(contextJsonObject.get("id"))))
        {
            referenceType = environment.lookupType(jsonPointer);
        }
        // If it has not, try to resolve it within the context document
        if (referenceType == null)
        {
            final String[] tokens = jsonPointer.split("/");
            JSONObject jsonObjectToken = contextJsonObject;

            for (int i = 1; i < tokens.length; i++)
            {
                if (jsonObjectToken.has(tokens[i]))
                {
                    jsonObjectToken = (JSONObject) jsonObjectToken.get(tokens[i]);
                }
                else
                {
                    jsonObjectToken = null;
                    break;
                }
            }
            if (jsonObjectToken != null)
            {
                referenceType = environment.evaluate(jsonObjectToken);
                environment.addType(jsonPointer, referenceType);
                return referenceType;
            }
        }
        else
        {
            return referenceType;
        }
        return null;
    }

    private SchemaEnv createSchemaEnv(SchemaEnv parentEnv, JSONObject remoteSchema) 
    {
        SchemaEnv environment = new SchemaEnv(parentEnv, remoteSchema);
        // register root type in new env
        JSONType rootType = environment.evaluate(remoteSchema);
        environment.addType(HASH, rootType);
        return environment;
    }


    public JSONObject getRemoteSchema(URL url)
    {
        String urlProtocol = url.getProtocol();
        if (urlProtocol.equals("file"))
        {

            try
            {
                if (url.toURI().isAbsolute())
                {
                    InputStream input = null;
                    try
                    {
                        input = url.openStream();
                        java.lang.String fileSchemaString = IOUtils.toString(input);
                        JSONObject refSchemaObject = new JSONObject(fileSchemaString);
                        return refSchemaObject;
                    }
                    catch (IOException e)
                    {
                        throw new SchemaException(e);
                    }
                    finally
                    {
                        IOUtils.closeQuietly(input);
                    }
                }
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }

        }
        else if (urlProtocol.equals("http") || urlProtocol.equals("https"))
        {

            BufferedReader rd = null;
            try
            {
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final String result = IOUtils.toString(rd);
                return new JSONObject(result);
            }
            catch (IOException e)
            {
                throw new SchemaException(e);
            }
            finally
            {
                IOUtils.closeQuietly(rd);
            }


        }
        return null;
    }


    @Override
    public boolean contains(Object obj)
    {
        return false;
    }

    @Override
    public java.lang.String explain(Object obj)
    {
        return null;
    }

    @Override
    public boolean isOptional()
    {
        return false;
    }

    @Override
    public boolean isJSONPrimitive()
    {
        return false;
    }

    @Override
    public boolean isJSONArray()
    {
        return false;
    }

    @Override
    public boolean isJSONObject()
    {
        return false;
    }

    @Override
    public boolean isJSONPointer()
    {
        return true;
    }

}