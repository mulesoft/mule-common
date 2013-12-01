/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;

import java.util.*;

/**
 * Model for representing dynamic maps with string keys
 */

public class DefaultDefinedMapMetaDataModel extends AbstractMetaDataModel implements DefinedMapMetaDataModel
{
    private List<MetaDataField> fields;
    private String name;

    @Deprecated
    public DefaultDefinedMapMetaDataModel(Map<String, ? extends MetaDataModel> metaDataModelMap, String name)
    {
        super(DataType.MAP);
        this.fields = this.convertMapToList(metaDataModelMap);
        this.name = name;
    }

    @Deprecated
    public DefaultDefinedMapMetaDataModel(Map<String, ? extends MetaDataModel> metaDataModelMap)
    {
        this(metaDataModelMap, null);
    }

    public DefaultDefinedMapMetaDataModel(List<MetaDataField> fields, String name)
    {
        super(DataType.MAP);
        this.fields = fields;
        this.name = name;
    }

    public DefaultDefinedMapMetaDataModel(List<MetaDataField> fields)
    {
        this(fields, null);
    }

    private List<MetaDataField> convertMapToList(Map<String, ? extends MetaDataModel> metaDataModelMap){
        List<MetaDataField> mappedFields = new ArrayList<MetaDataField>();
        for (Map.Entry<String, ? extends MetaDataModel> entry : metaDataModelMap.entrySet()) {
            mappedFields.add(new DefaultMetaDataField(entry.getKey(), entry.getValue()));
        }
        return mappedFields;
    }


    public Set<String> getKeys()
    {
        Set<String> result = new HashSet<String>();
        for(MetaDataField mdf : this.getFields()){
            result.add(mdf.getName());
        }
        return result;
    }

    public MetaDataModel getKeyMetaDataModel() {
        return new DefaultSimpleMetaDataModel(DataType.STRING);
    }

    public MetaDataModel getValueMetaDataModel(String key) 
    {
        for(MetaDataField mdf : this.getFields()){
            if (mdf.getName().equals(key)){
                return mdf.getMetaDataModel();
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public List<MetaDataField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((fields == null) ? 0 : fields.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof DefaultDefinedMapMetaDataModel)) return false;
        DefaultDefinedMapMetaDataModel other = (DefaultDefinedMapMetaDataModel) obj;
        if (fields == null)
        {
            if (other.fields != null) return false;
        }
        else if (!fields.equals(other.fields)) return false;
        if (name == null)
        {
            if (other.name != null) return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }

    public void accept(MetaDataModelVisitor modelVisitor) {
        modelVisitor.visitDynamicMapModel(this);
    }
}
