package org.mule.common.metadata.parser.json;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SchemaEnv
{


    private SchemaEnv parent;
    private Map<String, JSONType> types;
    private JSONObject contextJsonObject;

    public SchemaEnv()
    {
        parent = null;
        contextJsonObject = null;
        types = new TreeMap<String, JSONType>();
        types.put("string", new JSONType.StringType());
        types.put("integer", new JSONType.IntegerType());
        types.put("double", new JSONType.DoubleType());
        types.put("boolean", new JSONType.BooleanType());
        types.put("number", new JSONType.NumberType());
    }


    public SchemaEnv(SchemaEnv p)
    {
        this();
        this.parent = p;
    }

    public SchemaEnv(SchemaEnv p, JSONObject contextJsonObject)
    {
        this(p);
        this.contextJsonObject = contextJsonObject;
    }

    public JSONObject getContextJsonObject()
    {
        return contextJsonObject;
    }

    public JSONType lookupType(String name)
    {
        if (types.containsKey(name))
        {
            return types.get(name);
        }
        else if (parent != null)
        {
            return parent.lookupType(name);
        }
        else
        {
            return null;
        }
    }

    /**
     * Primary entry point to the JSONType parsing framework.
     * This method builds JSONType object from the given argument, relative to the
     * bindings in this environment.  String objects in the appropriate places
     * are looked up and converted to the corresponding bound JSONType objects.
     * <p/>
     * Everything else is recursively evaluated, by type.
     *
     * @param obj The schema expression, either a String or a JSONObject.
     * @return The JSONType object corresponding to the given schema expression.
     * @throws SchemaException
     * @throws IllegalArgumentException if the supplied argument is null, or if it is not of the correct type.
     */
    public JSONType evaluate(Object obj) throws SchemaException
    {
        try
        {
            if (obj == null)
            {
                throw new IllegalArgumentException("null schema object.");

            }
            else if (obj instanceof String)
            {

                // String objects are merely looked up in this environment, and returned
                // if found (or a SchemaException thrown, if not).

                String name = (String) obj;
                JSONType type = lookupType(name);
                if (type == null)
                {
                    throw new SchemaException(String.format("Unknown type name \"%s\"", name));
                }
                return type;

            }
            else if (obj instanceof JSONObject)
            {
                JSONObject json = (JSONObject) obj;

                boolean optional = json.has("optional") && json.getBoolean("optional");
                JSONType specifiedType = null;

                if (json.has("type"))
                {

                    Object rawType = json.get("type");
                    if (rawType instanceof JSONArray)
                    {//If the type of the object is an Array of types
                        specifiedType = new JSONType.Everything();
                    }
                    else if (rawType instanceof String)
                    {//If the type of the object is a string, i.e.: array, object
                        String type = json.getString("type").toLowerCase();
                        if (type.equals("array"))
                        {
                            specifiedType = new JSONArrayType(new SchemaEnv(this, contextJsonObject), json);
                        }
                        else if (type.equals("object"))
                        {

                            // we pass children of this environment, i.e.
                            // new SchemaEnv(this)
                            // rather than just
                            // this
                            // because JSONObjectType will bind itself into the Schema, and we don't
                            // want those names to be globally visible -- only local visible to the
                            // schema's sub-expressions.
                            specifiedType = new JSONObjectType(new SchemaEnv(this, contextJsonObject), json);
                        }
                        else
                        {//If the type is a string but not an array nor object
                            JSONType tt = lookupType(type);
                            if (tt == null)
                            {
                                throw new SchemaException(String.format("Unrecognized schema type %s in %s", type, json.toString()));
                            }
                            specifiedType = tt;
                        }
                    }
                }
                else if (json.has("properties"))
                {
                    specifiedType = new JSONObjectType(new SchemaEnv(this, contextJsonObject), json);
                }
                else if (json.has("enum"))
                {
                    specifiedType = new JSONType.StringType();
                }
                else if (json.has("$ref"))
                {
                    String reference = json.getString("$ref");
                    specifiedType = new JSONPointerType(this, reference);
                }
                else if (json.has("anyOf") || json.has("allOf") || json.has("oneOf"))
                {
                    specifiedType = new JSONType.Everything();
                }
                else
                {
                    throw new SchemaException("Schema object  doesn't contain a 'types' property." + "\n At : " + json.toString());
                }

                return optional ? new OptionalType(specifiedType) : specifiedType;

            }
            else if (obj instanceof JSONArray)
            {//For tuple typing. Return string
                return new JSONType.Everything();
            }
            else
            {
                throw new IllegalArgumentException(obj.getClass().getSimpleName() + " isn't a valid schema type");
            }
        }
        catch (JSONException e)
        {

            // Right now, the only time this is thrown is if the JSONObject schema expression
            // doesn't have a 'type' property.
            throw new SchemaException(String.valueOf(obj), e);
        }
    }


    public void addType(String name, JSONType t)
    {
        /*
        if(types.containsKey(name)) {
			throw new IllegalArgumentException(String.format("Type for name %s already exists.", name));
		}
		*/
        types.put(name, t);
    }

    public boolean containsType(String name)
    {
        return types.containsKey(name);
    }

    public Iterator<String> names()
    {
        return new TreeSet<String>(types.keySet()).iterator();
    }

    public SchemaEnv getParent()
    {
        return parent;
    }


}
