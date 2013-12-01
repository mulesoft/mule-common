/**
 *
 */
package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;

public class DefaultExampleBasedMetaDataModel extends AbstractMetaDataModel implements ExampleBasedMetaDataModel
{

    private String example;

    public DefaultExampleBasedMetaDataModel(DataType dataType, String example)
    {
        super(dataType);
        this.example = example;
    }

    public String getExampleContent()
    {
        return example;
    }

    public void accept(MetaDataModelVisitor modelVisitor)
    {
    }

}
