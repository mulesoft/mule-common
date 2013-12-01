package org.mule.common.metadata.field.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Property used to represent a list of valid enum values.
 */
public class ValidStringValuesFieldProperty implements MetaDataFieldProperty {

    private List<String> validStrings = new ArrayList<String>();

    public ValidStringValuesFieldProperty() {
    }

    public ValidStringValuesFieldProperty(String... values) {
        validStrings.addAll(Arrays.asList(values));
    }

    public List<String> getValidStrings() {
        return validStrings;
    }

    public void setValidStrings(List<String> validStrings) {
        this.validStrings = validStrings;
    }

    public void addValue(String value) {
        this.validStrings.add(value);
    }
}