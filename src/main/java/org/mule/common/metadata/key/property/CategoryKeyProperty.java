package org.mule.common.metadata.key.property;

public class CategoryKeyProperty implements MetaDataKeyProperty {
    private String name;

    public CategoryKeyProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryKeyProperty)) return false;

        CategoryKeyProperty that = (CategoryKeyProperty) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}