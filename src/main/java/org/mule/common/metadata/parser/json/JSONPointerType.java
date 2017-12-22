package org.mule.common.metadata.parser.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class JSONPointerType implements JSONType
{
    private static final String ID = "id";
    public static final String HASH = "#";
    private String ref;
    private SchemaEnv env;

    public JSONPointerType(SchemaEnv env, String reference)
    {
        this.ref = reference;
        this.env = env;
    }

    public JSONType resolve()
    {
        JSONType referenceType = null;
        String baseURI = "";
        String jsonPointer = HASH;
        String[] splitRef = ref.split(HASH);

        // first try to look if in the context of this ref exists any object with an id that matches equally to this ref. It doesn't matter if the id is actually a URL to a json
        // schema, if another object has the same id, the ref should resolve to that object
        JSONObject literalRef = findAnyObjectWithId(env.getContextJsonObject(), ref);
        if (literalRef != null)
        {
            referenceType = env.evaluate(literalRef);
            env.addType(ref, referenceType);
        }
        else
        {
            switch (splitRef.length)
            {
            case 1:
                baseURI = splitRef[0];
                jsonPointer = HASH;
                break;
            case 2:
                baseURI = splitRef[0];
                jsonPointer = splitRef[1];
                break;
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
                    environment = createSchemaEnv(env, remoteSchema, url);
                }
                catch (MalformedURLException e)
                {
                    // Try to get schema from a file in relative path
                    URL contextJsonURL = env.getContextJsonURL();
                    URL urlFile;
                    try
                    {
                        urlFile = new URL(contextJsonURL, baseURI);
                        remoteSchema = getRemoteSchema(urlFile);
                        environment = createSchemaEnv(env, remoteSchema, urlFile);
                    }
                    catch (MalformedURLException e1)
                    {
                        throw new SchemaException(e1);
                    }

                }
            }

            if (HASH.equals(jsonPointer))
            {
                referenceType = environment.lookupType(HASH);
            }
            else
            {
                // See if it has already been resolved if base URI is empty or matches this schema's id
                JSONObject contextJsonObject = environment.getContextJsonObject();
                if (baseURI.equals("") || (contextJsonObject.has(ID) && baseURI.equals(contextJsonObject.get(ID))))
                {
                    referenceType = environment.lookupType(jsonPointer);
                }
                // If it has not, try to resolve it within the context document
                if (referenceType == null)
                {
                    JSONObject jsonObjectToken = findByPath(jsonPointer, contextJsonObject);
                    if (jsonObjectToken != null)
                    {
                        referenceType = environment.evaluate(jsonObjectToken);
                        environment.addType(jsonPointer, referenceType);
                    }
                }
            }
        }
        return referenceType;
    }

    private JSONObject findAnyObjectWithId(JSONObject contextJsonObject, String id)
    {
        JSONObject result = null;
        if (contextJsonObject.has(ID) && contextJsonObject.get(ID).equals(id))
        {
            result = contextJsonObject;
        }
        else
        {
            Iterator<?> keys = contextJsonObject.keys();
            while (keys.hasNext() && result == null)
            {
                Object next = keys.next();
                if (next instanceof String)
                {
                    String nextKey = (String) next;
                    Object nextValue = contextJsonObject.get(nextKey);
                    if (nextValue instanceof JSONObject)
                    {
                        JSONObject jsonObject = (JSONObject) nextValue;
                        result = findAnyObjectWithId(jsonObject, id);
                    }
                }
            }
        }
        return result;
    }

    private JSONObject findByPath(String jsonPointer, JSONObject contextJsonObject) {
        JSONObject jsonObjectToken = contextJsonObject;
        final String[] tokens = jsonPointer.split("/");

        JSONObject result = null;
        for (int i = 1; i < tokens.length; i++)
        {
            String token = tokens[i];
            if (jsonObjectToken.has(token))
            {
                result = jsonObjectToken = (JSONObject) jsonObjectToken.get(token);
            }
            else
            {
                result = null;
                break;
            }
        }
        return result;
    }

    private SchemaEnv createSchemaEnv(SchemaEnv parentEnv, JSONObject remoteSchema, URL contextUrl)
    {
        SchemaEnv environment = new SchemaEnv(parentEnv, remoteSchema, contextUrl);
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