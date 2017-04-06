package org.mule.common.metadata;

import org.apache.commons.lang.ObjectUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 */
public class MetaDataProperties
{

    private Set<MetaDataField> fields;

    public MetaDataProperties(Set<MetaDataField> fields)
    {
        this.fields = fields;
    }

    public MetaDataProperties()
    {
        this(new TreeSet<MetaDataField>(new MetaDataFieldComparator()));
    }

    public Set<MetaDataField> getFields()
    {
        return fields;
    }

    public void add(MetaDataField field)
    {
        fields.add(field);
    }

    public void addAll(MetaDataProperties properties)
    {
        fields.addAll(properties.fields);
    }


    public Map<String, MetaDataModel> toMap()
    {
        final Map<String, MetaDataModel> result = new HashMap<String, MetaDataModel>();
        for (MetaDataField field : fields)
        {
            result.put(field.getName(), field.getMetaDataModel());
        }
        return result;
    }


    public MetaDataField getFieldByName(String name)
    {

        for (MetaDataField field : fields)
        {
            if (field.getName().equals(name))
            {
                return field;
            }
        }
        return null;
    }


    public boolean removeFieldByName(String name)
    {

        for (MetaDataField field : fields)
        {
            if (field.getName().equals(name))
            {
                this.fields.remove(field);
                return true;
            }
        }
        return false;
    }

    private static class MetaDataFieldComparator implements Comparator<MetaDataField> {

        @Override
        public int compare(MetaDataField field1, MetaDataField field2) {
            return ObjectUtils.compare(field1.getName(), field2.getName());
        }
    }
}