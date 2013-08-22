package org.mule.common.metadata.field.property;

import org.mule.common.metadata.field.property.exception.RepeatedFieldPropertyException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * It ensures that for a given field, the properties of it will never collide.
 * Also, it has some helper methods to access the different properties in it
 */
public class  MetaDataFieldPropertyManager {

    private List<MetaDataFieldProperty> fieldProperties;

    public MetaDataFieldPropertyManager(List<MetaDataFieldProperty> fieldProperties) {
        this.setFieldProperties(fieldProperties);
    }

    public boolean hasProperty(Class<? extends MetaDataFieldProperty> fieldProperty){
        return this.getProperty(fieldProperty) != null;
    }

    /**
     * @param fieldPropertyClass class to look for
     * @return a MetaDataFieldProperty if the current manager has a fieldPropertyClass, null otherwise
     */
    @SuppressWarnings("unchecked")
	public <T extends MetaDataFieldProperty> T getProperty(Class<T> fieldPropertyClass){
        for (MetaDataFieldProperty fc : this.fieldProperties){
            if (fieldPropertyClass.equals(fc.getClass())){
                return (T) fc;
            }
        }
        return null;
    }

    /**
     * Adds a property if the class of metaDataFieldProperty was not already used
     * in the current manager
     * @param fieldProperty new property to be added
     * @return true if it was added successfully, false otherwise
     */
    public boolean addProperty(MetaDataFieldProperty fieldProperty){
       if (! hasProperty(fieldProperty.getClass())){
           return this.fieldProperties.add(fieldProperty);
       }
       return false;
    }

    /**
     * Removes a property if exists in the current manager
     * @param FieldProperty
     * @return  true if it was removed successfully, false otherwise
     */
    public boolean removeProperty(MetaDataFieldProperty FieldProperty){
        if ( this.fieldProperties.contains(FieldProperty) ){
            return this.fieldProperties.remove(FieldProperty);
        }
        return false;
    }

    /**
     * @return a unmodifiable list of properties
     */
    public List<MetaDataFieldProperty> getProperties() {
        return Collections.unmodifiableList(fieldProperties);
    }

    private void setFieldProperties(List<MetaDataFieldProperty> fieldProperties){
        if (hasRepeatedFieldProperties(fieldProperties)){
            throw new RepeatedFieldPropertyException("Field capabilities must no be repeated");
        }
        this.fieldProperties = fieldProperties;
    }

    private boolean hasRepeatedFieldProperties(List<MetaDataFieldProperty> fieldProperties){
        Set<Class<?>> clazzes = new HashSet<Class<?>>();
        for (MetaDataFieldProperty fc: fieldProperties){
            clazzes.add(fc.getClass());
        }
        return clazzes.size() != fieldProperties.size();
    }
}
