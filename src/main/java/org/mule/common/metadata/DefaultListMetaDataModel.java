package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;

public class DefaultListMetaDataModel extends AbstractMetaDataModel implements ListMetaDataModel {

    private MetaDataModel model;
    private boolean isArray;

    public DefaultListMetaDataModel(MetaDataModel model) {
        this(model, false);
    }

    public DefaultListMetaDataModel(MetaDataModel model, boolean isArray) {
        super(DataType.LIST);
        this.model = model;
        this.isArray = isArray;
    }

    public MetaDataModel getElementModel() {
        return model;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        return result;
    }
    
    public boolean isArray() {
        return isArray;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof DefaultListMetaDataModel)) return false;
        DefaultListMetaDataModel other = (DefaultListMetaDataModel) obj;
        if (model == null)
        {
            if (other.model != null) return false;
        }
        else if (!model.equals(other.model)) return false;
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DefaultListMetaDataModel: { [");
        sb.append(System.getProperty("line.separator"));
        sb.append("  metaDataModel: ");
        sb.append(model != null ? model.toString() : "null");
        sb.append(System.getProperty("line.separator"));
        sb.append("] }");
        return sb.toString();
    }

    public void accept(MetaDataModelVisitor modelVisitor) {
        modelVisitor.visitListMetaDataModel(this);
    }

}
