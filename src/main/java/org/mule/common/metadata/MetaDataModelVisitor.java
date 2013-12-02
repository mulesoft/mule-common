package org.mule.common.metadata;

/**
 * visitor for MetaDataModel
 */

public interface MetaDataModelVisitor {
    void visitPojoModel(PojoMetaDataModel pojoMetaDataModel);

    void visitListMetaDataModel(ListMetaDataModel listMetaDataModel);

    void visitSimpleMetaDataModel(SimpleMetaDataModel simpleMetaDataModel);

    void visitStaticMapModel(ParameterizedMapMetaDataModel parameterizedMapMetaDataModel);

    void visitDynamicMapModel(DefinedMapMetaDataModel definedMapMetaDataModel);

    void visitXmlMetaDataModel(XmlMetaDataModel xmlMetaDataModel);
}
