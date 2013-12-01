package org.mule.common.metadata.field.property;

/**
 * Use this property to store a string example of the corresponding type.
 * For example, use it to store the contents of an xml, csv or json.
 *
 */
public class ExampleFieldProperty implements MetaDataFieldProperty {

    private String exampleValue = "";
    
    public ExampleFieldProperty() {
    }

    public ExampleFieldProperty(String exampleValue) {
        this.exampleValue = exampleValue;
    }

    public String getExampleValue() {
        return exampleValue;
    }

    public void setExampleValue(String exampleValue) {
        this.exampleValue = exampleValue;
    }
}
