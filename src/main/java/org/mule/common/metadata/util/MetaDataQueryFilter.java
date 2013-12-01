package org.mule.common.metadata.util;

import org.mule.common.metadata.*;
import org.mule.common.query.Field;

import java.util.List;

/**
 * A class used to filter up fields from a metadata object
 */
public class MetaDataQueryFilter {

    private MetaData metaData;
    private List<Field> fields;

    public MetaDataQueryFilter(MetaData metaData, List<Field> fields) {
        this.metaData = metaData;
        this.fields = fields;
    }

    public MetaData doFilter(){
        MetaDataModel model = metaData.getPayload();
        MetaDataQueryFilterVisitor modelVisitor = new MetaDataQueryFilterVisitor(fields);
        model.accept(modelVisitor);
        return modelVisitor.filteringResult();
    }
}
