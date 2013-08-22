package org.mule.common.metadata;

import org.mule.common.metadata.field.property.MetaDataFieldProperty;

import java.util.List;

public interface MetaDataField {
	
	public String getName();

	public MetaDataModel getMetaDataModel();

	public FieldAccessType getAccessType();

    public List<MetaDataFieldProperty> getProperties();

    public boolean addProperty(MetaDataFieldProperty metaDataFieldProperty);

    public boolean removeProperty(MetaDataFieldProperty metaDataFieldProperty);

    public boolean hasProperty(Class<? extends MetaDataFieldProperty> metaDataFieldProperty);

    public <T extends MetaDataFieldProperty> T getProperty(Class<T> metaDataFieldProperty);

	public static enum FieldAccessType
	{
		READ, WRITE, READ_WRITE;
	}
}
