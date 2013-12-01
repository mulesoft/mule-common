package org.mule.common.metadata;

import java.util.List;
import java.util.Set;

import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.field.property.DefaultFieldPropertyFactory;
import org.mule.common.metadata.field.property.FieldPropertyFactory;

public class DefaultPojoMetaDataModel extends AbstractMetaDataModel implements PojoMetaDataModel {

    private String clazzName;
    private boolean isInterface;
    private List<MetaDataField> fieldsForClass;
    private String name;
    private Set<String> parentNames;

    public DefaultPojoMetaDataModel(Class<?> clazz) {
        this(clazz, clazz.getSimpleName(), MetaDataModelFactory.getInstance().getFieldsForClass(clazz, new DefaultFieldPropertyFactory()));
    }

    public DefaultPojoMetaDataModel(Class<?> clazz, FieldPropertyFactory fieldPropertyFactory) {
        this(clazz, clazz.getSimpleName(), MetaDataModelFactory.getInstance().getFieldsForClass(clazz, fieldPropertyFactory));
    }

    public DefaultPojoMetaDataModel(Class<?> clazz, List<MetaDataField> fields) {
        this(clazz, clazz.getSimpleName(),fields);
    }

    protected DefaultPojoMetaDataModel(Class<?> clazz, String name, List<MetaDataField> fields) {
        super(DataType.POJO);
        this.name = name;
        this.parentNames = MetaDataModelFactory.getInstance().getParentNames(clazz);
        this.clazzName = clazz.getName();
        this.isInterface = clazz.isInterface();
        this.fieldsForClass = fields;
        setImplementationClass(clazz.getName());
    }

    public String getClassName() {
        return clazzName;
    }

    public List<MetaDataField> getFields() {
        return fieldsForClass;
    }

    public boolean isInterface()
    {
        return isInterface;
    }

    @Override
    public String toString() {
        return "DefaultPojoMetaDataModel [clazzName=" + clazzName
                + ", isInterface=" + isInterface + ", fieldsForClass="
                + fieldsForClass + ", name=" + name + ", parentNames="
                + parentNames + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((clazzName == null) ? 0 : clazzName.hashCode());
        result = prime * result + (isInterface ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parentNames == null) ? 0 : parentNames.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultPojoMetaDataModel other = (DefaultPojoMetaDataModel) obj;
        if (clazzName == null) {
            if (other.clazzName != null)
                return false;
        } else if (!clazzName.equals(other.clazzName))
            return false;
        if (isInterface != other.isInterface)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (parentNames == null) {
            if (other.parentNames != null)
                return false;
        } else if (!parentNames.equals(other.parentNames))
            return false;
        return true;
    }

    public Set<String> getParentNames() {
        return parentNames;
    }

    public void accept(MetaDataModelVisitor modelVisitor) {
        modelVisitor.visitPojoModel(this);
    }
}
