package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.parser.json.JSONArrayType;
import org.mule.common.metadata.parser.json.JSONObjectType;
import org.mule.common.metadata.parser.json.JSONPointerType;
import org.mule.common.metadata.parser.json.JSONType;
import org.mule.common.metadata.parser.json.JSONTypeUtils;

import java.util.HashMap;
import java.util.Map;

class JSONMetadataModelFactoryHelper {

    private final Map<JSONObjectType, DefaultStructuredMetadataModel> visitedTypes;

    public JSONMetadataModelFactoryHelper(final Map<JSONObjectType, DefaultStructuredMetadataModel> visitedTypes) {
        this.visitedTypes = visitedTypes;
    }

    public JSONMetadataModelFactoryHelper() {
        this(new HashMap<JSONObjectType, DefaultStructuredMetadataModel>());
    }

    public AbstractMetaDataModel buildJSONArrayMetaDataModel(JSONArrayType property) {
        AbstractMetaDataModel model = null;
        JSONType itemsType = property.getItemsType();
        if (itemsType.isJSONPrimitive()) { // Case List<String>
            DataType dataType = JSONTypeUtils.getDataType(itemsType);
            model = dataType == DataType.UNKNOWN ? new DefaultUnknownMetaDataModel() : new DefaultSimpleMetaDataModel(dataType);
        } else {
            if(itemsType.isJSONPointer()){
                model = buildJSONPointerMetaDataModel((JSONPointerType) itemsType);
            }else if (itemsType.isJSONObject()){
                model = buildJSONMetaDataModel((JSONObjectType) itemsType);
            }
        }
        return model;
    }

    public AbstractMetaDataModel buildJSONPointerMetaDataModel(JSONPointerType pointer) {
        JSONType resolvedType = pointer.resolve();
        if (resolvedType.isJSONArray()) {
            return buildJSONArrayMetaDataModel((JSONArrayType) resolvedType);
        } else if (resolvedType.isJSONObject()) {
            return buildJSONMetaDataModel((JSONObjectType) resolvedType);
        } else if (resolvedType.isJSONPointer()) {
            return buildJSONPointerMetaDataModel((JSONPointerType) resolvedType);
        } else if (resolvedType.isJSONPrimitive()) {
            DataType dataType = JSONTypeUtils.getDataType(resolvedType);
            return dataType == DataType.UNKNOWN ? new DefaultUnknownMetaDataModel() : new DefaultSimpleMetaDataModel(dataType);
        }
        return null;
    }

    public DefaultStructuredMetadataModel buildJSONMetaDataModel(JSONObjectType type) {

        DefaultStructuredMetadataModel model;
        if (visitedTypes.containsKey(type)) {
            model = visitedTypes.get(type);
        } else {
            model = new DefaultStructuredMetadataModel(DataType.JSON);
            visitedTypes.put(type, model);
            model.loadFieldsFrom(new JSONSchemaMetaDataFieldFactory(type, this));
        }
        return model;
    }

}
