package org.mule.common.metadata.parser.json;

import static java.lang.String.format;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Timothy Danford
 */
public class JSONObjectType extends AbstractType { 

	private java.lang.String id;
	private java.lang.String name;
	private java.lang.String description;
	private boolean isStrict;
	private Map<java.lang.String,JSONType> properties;
    
	@SuppressWarnings("unchecked")
    public JSONObjectType(SchemaEnv env, JSONObject obj) throws SchemaException { 
		this.name = null;
		this.description = null;
		isStrict = false;
		properties = new TreeMap<java.lang.String,JSONType>();
		
		try {
		    //Add root to the types
		    if(env.getParent()==null){
		        env.addType("#", this);
		    }
		    //
		    if(!obj.has(JSONSchemaConstants.TYPE) && !obj.has(JSONSchemaConstants.PROPERTIES)) {
				throw new SchemaException(
						format("Missing required 'type' or 'properties' property in \n%s", obj.toString()));
			}
		    
		    if(obj.has(JSONSchemaConstants.TYPE)){
		        Object type = obj.get(JSONSchemaConstants.TYPE);
		        
		        if(!type.toString().toLowerCase().equals("object")) { 
		            //Check if the type is an array of types. In this case set string as the type of a field named "text".
		            if (type instanceof JSONArray){
		                properties.put("text", new StringType());
		            }else{
		                throw new SchemaException(java.lang.String.format("Cannot convert expression %s into JSONObjectType", obj.toString()));
		            }
		        }
		    }
			if(obj.has("strict")) { 
				isStrict = obj.getBoolean("strict");
			}
			
			if(obj.has("name")) { 
				java.lang.String name = obj.getString("name");
				this.name = name;
				env.addType(name, this);
			}
			
			if(obj.has("description")) { 
				this.description = obj.getString("description");
			}
			
			if(obj.has("id")){
			    this.setId(obj.getString("id"));
                java.lang.String id = this.getId().split("#")[0];
                env.addType(id, this);
			}
			
			// an object could either have a $ref or properties, if both are defined, $ref takes precedence
			if (obj.has(JSONSchemaConstants.$_REF)) {
			    String reference = obj.getString(JSONSchemaConstants.$_REF);
			    JSONType resolvedReference = new JSONPointerType(env, reference).resolve();
			    if (resolvedReference instanceof JSONObjectType) {
			        JSONObjectType jsonObjectType = (JSONObjectType) resolvedReference;
			        String[] jsonObjectProperties = jsonObjectType.getProperties();
			        for (String property : jsonObjectProperties) {
			            JSONType propertyType = jsonObjectType.getPropertyType(property);
			            properties.put(property, propertyType);
			        }
			    }
			} else if (obj.has(JSONSchemaConstants.PROPERTIES)) {
				JSONObject propObj = obj.getJSONObject(JSONSchemaConstants.PROPERTIES);
				Iterator<java.lang.String> itr = (Iterator<java.lang.String>)propObj.keys();
				while(itr.hasNext()) { 
					java.lang.String propName = itr.next();
					Object propValue = propObj.get(propName);
					JSONType propType = env.evaluate(propValue);
                    properties.put(propName, propType);                  
				}
			}			
		} catch (JSONException e) { 
			throw new SchemaException(obj.toString(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
    public boolean contains(Object obj) { 
		if(obj == null || !(obj instanceof JSONObject)) { 
			return false; 
		}

		JSONObject json = (JSONObject)obj;
		Iterator<java.lang.String> keys = json.keys();
		Set<java.lang.String> toSee = new TreeSet<java.lang.String>(properties.keySet());
		Set<java.lang.String> seen = new TreeSet<java.lang.String>();

		while(keys.hasNext()) { 
			java.lang.String key = keys.next();
			if(seen.contains(key)) { 
				return false;
			}

			if(toSee.contains(key)) { 
				try { 
					if(!properties.get(key).contains(json.get(key))) { 
						return false;
					}
				} catch(JSONException e) { 
					throw new IllegalArgumentException(java.lang.String.format(
							"%s in %s", key, obj.toString()));
				}

				toSee.remove(key);
				seen.add(key);
				
			} else if (isStrict) {
				// 'strict' schemas will fail if there are any extra fields.
				return false;  
			}
		}

		for(java.lang.String undefinedKey : toSee) {  
			if(!properties.get(undefinedKey).isOptional()) { 
				return false;
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("rawtypes")
    public java.lang.String explain(Object obj) {  
		if (obj == null) { 
			return format("REJECT: null value");
		} else if (!(obj instanceof JSONObject)) { 
			return format("REJECT: value %s has type %s not in JSONObject", 
					obj.toString(), obj.getClass().getSimpleName());
		} else { 
			JSONObject json = (JSONObject)obj;
			Iterator keys = json.keys();
			Set<java.lang.String> toSee = new TreeSet<java.lang.String>(properties.keySet());
			Set<java.lang.String> seen = new TreeSet<java.lang.String>();
			
			while(keys.hasNext()) { 
				java.lang.String key = (java.lang.String)keys.next();
				
				if(seen.contains(key)) { 
					return format("REJECT: unexpected duplicate key \"%s\" in %s", key, json.toString());
				}

				if(toSee.contains(key)) { 
					try { 
						if(!properties.get(key).contains(json.get(key))) { 
							return java.lang.String.format("%s: %s", 
									key, properties.get(key).explain(json.get(key)));
						}
					} catch(JSONException e) { 
						throw new IllegalArgumentException(java.lang.String.format(
								"%s in %s", key, obj.toString()));
					}

					toSee.remove(key);
					seen.add(key);
				} else if (isStrict) {
					// 'strict' schemas will fail if there are any extra fields.
					return java.lang.String.format("REJECT: extra key %s in 'strict' type %s",
							key, name);
				}
			}
			
			for(java.lang.String undefinedKey : toSee) {  
				if(!properties.get(undefinedKey).isOptional()) { 
					return format("REJECT: missing non-optional key: %s", 
							undefinedKey);
				}
			}
			
		}
		return null;
	}

	public java.lang.String getName() {
		return name;
	}
	
	public java.lang.String getDescription() { 
		return description;
	}
	
	public java.lang.String[] getProperties() { 
		return properties.keySet().toArray(new java.lang.String[0]);
	}
	
	public JSONType getPropertyType(java.lang.String propName) { 
		return properties.get(propName);
	}

    @Override
    public boolean isJSONPrimitive() {
        return false;
    }

    @Override
    public boolean isJSONArray() {
        return false;
    }

    @Override
    public boolean isJSONObject() {
        return true;
    }

    @Override
    public boolean isJSONPointer() {
        return false;
    }


    public java.lang.String getId() {
        return id;
    }


    public void setId(java.lang.String id) {
        this.id = id;
    }
}
