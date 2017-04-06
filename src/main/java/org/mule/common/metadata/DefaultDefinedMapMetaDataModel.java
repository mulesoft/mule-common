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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * <p>Dynamic Object metadata representation</p>
 * <p>Shouldn't use this directly. Use {@link org.mule.common.metadata.builder.DefaultMetaDataBuilder} instead.</p>
 */

public class DefaultDefinedMapMetaDataModel extends AbstractStructuredMetaDataModel implements DefinedMapMetaDataModel {

    private String name;

    @Deprecated
    public DefaultDefinedMapMetaDataModel(Map<String, ? extends MetaDataModel> metaDataModelMap, String name) {
        super(DataType.MAP, convertMapToList(metaDataModelMap));
        this.name = name;
    }

    @Deprecated
    public DefaultDefinedMapMetaDataModel(Map<String, ? extends MetaDataModel> metaDataModelMap) {
        this(metaDataModelMap, null);
    }

    public DefaultDefinedMapMetaDataModel(List<MetaDataField> fields) {
        this(fields, null);
    }

    public DefaultDefinedMapMetaDataModel(List<MetaDataField> fields, String name) {
        super(DataType.MAP, fields);
        this.name = name;
    }

    private static List<MetaDataField> convertMapToList(Map<String, ? extends MetaDataModel> metaDataModelMap) {
        List<MetaDataField> mappedFields = new ArrayList<MetaDataField>();
        Map<String, ? extends MetaDataModel> metaDataModelMapOrderByName = new TreeMap<String, MetaDataModel>(metaDataModelMap);
        for (Map.Entry<String, ? extends MetaDataModel> entry : metaDataModelMapOrderByName.entrySet()) {
            mappedFields.add(new DefaultMetaDataField(entry.getKey(), entry.getValue()));
        }
        return mappedFields;
    }


    @Override
    public Set<String> getKeys() {
        Set<String> result = new HashSet<String>();
        for (MetaDataField mdf : this.getFields()) {
            result.add(mdf.getName());
        }
        return result;
    }

    @Override
    public MetaDataModel getKeyMetaDataModel() {
        return new DefaultSimpleMetaDataModel(DataType.STRING);
    }

    @Override
    public MetaDataModel getValueMetaDataModel(String key) {
        for (MetaDataField mdf : this.getFields()) {
            if (mdf.getName().equals(key)) {
                return mdf.getMetaDataModel();
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((getFields() == null) ? 0 : getFields().hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof DefaultDefinedMapMetaDataModel)) return false;
        DefaultDefinedMapMetaDataModel other = (DefaultDefinedMapMetaDataModel) obj;
        if (getFields() == null) {
            if (other.getFields() != null) return false;
        } else if (!getFields().equals(other.getFields())) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public void accept(MetaDataModelVisitor modelVisitor) {
        modelVisitor.visitDynamicMapModel(this);
    }
}


