package org.mule.common.metadata.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mule.common.testutils.MuleMatchers.isExactlyA;

import org.mule.common.metadata.*;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.builder.ListMetaDataBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.datatype.SupportedOperatorsFactory;
import org.mule.common.metadata.property.DescriptionMetaDataProperty;
import org.mule.common.metadata.property.LabelMetaDataProperty;
import org.mule.common.metadata.field.property.ValidStringValuesFieldProperty;
import org.mule.common.metadata.field.property.dsql.DsqlOrderMetaDataFieldProperty;
import org.mule.common.metadata.field.property.dsql.DsqlQueryOperatorsMetaDataFieldProperty;
import org.mule.common.metadata.field.property.dsql.DsqlSelectMetaDataFieldProperty;
import org.mule.common.metadata.field.property.dsql.DsqlWhereMetaDataFieldProperty;
import org.mule.common.metadata.test.pojo.EverythingPojo;
import org.mule.common.query.expression.EqualsOperator;
import org.mule.common.query.expression.LikeOperator;
import org.mule.common.query.expression.Operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 */
public class DefaultDefinedMapMetaDataModelTestCase {
    public enum EnumTest {
        ENUM_FIELD1, ENUM_FIELD2, ENUM_FIELD3;
    }

	private DefinedMapMetaDataModel mapMetaDataModel;

	@Before
	public void setUp() {
		Map<String, MetaDataModel> map = new HashMap<String, MetaDataModel>();
		map.put("field1", new DefaultSimpleMetaDataModel(DataType.STRING));
		map.put("field2", new DefaultSimpleMetaDataModel(DataType.NUMBER));

		mapMetaDataModel = new DefaultDefinedMapMetaDataModel(map, "mapName");
	}

	@Test
	public void testKeys() {
		assertTrue(mapMetaDataModel.getKeys().contains("field1"));
		assertTrue(mapMetaDataModel.getKeys().contains("field2"));
	}

	@Test
	public void testValues() {
		assertTrue(mapMetaDataModel.getValueMetaDataModel("field1").getDataType().equals(DataType.STRING));
		assertTrue(mapMetaDataModel.getValueMetaDataModel("field2").getDataType().equals(DataType.NUMBER));
	}

	@Test
	public void testFields() {
		assertNotNull(mapMetaDataModel.getFields());
		assertEquals(2, mapMetaDataModel.getFields().size());
		Assertions.assertThat(mapMetaDataModel.getFields()).extracting("metaDataModel").extracting("dataType").contains(DataType.STRING);
		Assertions.assertThat(mapMetaDataModel.getFields()).extracting("metaDataModel").extracting("dataType").contains(DataType.NUMBER);
	}

	@Test
	public void whenCreatingModelFromMapsWithMapsFieldsShouldBeCreatedCorrectly() {

		final DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
		container.addSimpleField("simpleProperty", DataType.STRING).addDynamicObjectField("subComplexProperty").addSimpleField("subSimpleProperty", DataType.STRING)
				.addDynamicObjectField("subSubComplexProperty").addSimpleField("subSubSimpleProperty", DataType.STRING);

		final DefinedMapMetaDataModel metaDataModel = container.build();
		final List<MetaDataField> fields = metaDataModel.getFields();
		Assert.assertThat(fields.size(), is(2));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("simpleProperty").getDataType(), is(DataType.STRING));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("subComplexProperty").getDataType(), is(DataType.MAP));

		final DefinedMapMetaDataModel subComplexProperty = (DefinedMapMetaDataModel) metaDataModel.getValueMetaDataModel("subComplexProperty");
		Assert.assertThat(subComplexProperty.getFields().size(), is(2));

		Assert.assertThat(subComplexProperty.getValueMetaDataModel("subSimpleProperty").getDataType(), is(DataType.STRING));
		Assert.assertThat(subComplexProperty.getValueMetaDataModel("subSubComplexProperty").getDataType(), is(DataType.MAP));

		final DefinedMapMetaDataModel subSubComplexProperty = (DefinedMapMetaDataModel) subComplexProperty.getValueMetaDataModel("subSubComplexProperty");

		Assert.assertThat(subSubComplexProperty.getFields().size(), is(1));
		Assert.assertThat(subSubComplexProperty.getValueMetaDataModel("subSubSimpleProperty").getDataType(), is(DataType.STRING));

	}

	@Test
	public void whenCreatingModelWithEnumFieldsShouldBeCreatedCorrectly() {
		final DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
		container.addSimpleField("simpleProperty", DataType.STRING)
		.addEnumField("enumField").setValues("value1", "value2", "value3")
		.addSimpleField("intField", DataType.INTEGER);

		final DefinedMapMetaDataModel metaDataModel = container.build();
		final List<MetaDataField> fields = metaDataModel.getFields();
		Assert.assertThat(fields.size(), is(3));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("simpleProperty").getDataType(), is(DataType.STRING));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("enumField").getDataType(), is(DataType.ENUM));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("intField").getDataType(), is(DataType.INTEGER));

		Assert.assertThat(metaDataModel.getFields().get(1), notNullValue());
		MetaDataField metaDataField = metaDataModel.getFields().get(1);
        Assert.assertThat("When undefined implementation class, the default must return the same as the DataType", metaDataField.getMetaDataModel().getImplementationClass(), is(DataType.ENUM.getDefaultImplementationClass()));

		Assert.assertThat(metaDataField.getProperty(ValidStringValuesFieldProperty.class), notNullValue());
		ValidStringValuesFieldProperty property = metaDataField.getProperty(ValidStringValuesFieldProperty.class);
		Assert.assertThat(property.getValidStrings().size(), is(3));
		List<String> validStrings = property.getValidStrings();

		Assert.assertThat(validStrings.get(0), equalTo("value1"));
		Assert.assertThat(validStrings.get(1), equalTo("value2"));
		Assert.assertThat(validStrings.get(2), equalTo("value3"));
	}


    @Test
    public void whenCreatingModelWithEnumFieldsAndImplementationClassShouldBeCreatedCorrectly() {
        List<String> values = new ArrayList<String>();
        for (EnumTest enumTest : EnumTest.values()) {
            values.add(enumTest.toString());
        }


        final DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
        container.addSimpleField("simpleProperty", DataType.STRING)
                .addEnumField("enumField", EnumTest.class.getName()).setValues(values)
                .addSimpleField("intField", DataType.INTEGER);

        final DefinedMapMetaDataModel metaDataModel = container.build();
        final List<MetaDataField> fields = metaDataModel.getFields();
        Assert.assertThat(fields.size(), is(3));
        Assert.assertThat(metaDataModel.getValueMetaDataModel("simpleProperty").getDataType(), is(DataType.STRING));
        Assert.assertThat(metaDataModel.getValueMetaDataModel("enumField").getDataType(), is(DataType.ENUM));
        Assert.assertThat(metaDataModel.getValueMetaDataModel("intField").getDataType(), is(DataType.INTEGER));

        Assert.assertThat(metaDataModel.getFields().get(1), notNullValue());
        MetaDataField metaDataField = metaDataModel.getFields().get(1);
        Assert.assertThat("When defined implementation class, the value must be the specified value", metaDataField.getMetaDataModel().getImplementationClass(), is(EnumTest.class.getName()));

        Assert.assertThat(metaDataField.getProperty(ValidStringValuesFieldProperty.class), notNullValue());
        ValidStringValuesFieldProperty property = metaDataField.getProperty(ValidStringValuesFieldProperty.class);
        Assert.assertThat(property.getValidStrings().size(), is(3));
        List<String> validStrings = property.getValidStrings();

        Assert.assertThat(validStrings.get(0), equalTo("ENUM_FIELD1"));
        Assert.assertThat(validStrings.get(1), equalTo("ENUM_FIELD2"));
        Assert.assertThat(validStrings.get(2), equalTo("ENUM_FIELD3"));
    }

	@Test
	public void whenCreatingModelFromMapsWithListOfMapsFieldsShouldBeCreatedCorrectly() {

		final DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
		container.addSimpleField("simpleProperty", DataType.STRING).addListOfDynamicObjectField("subListComplexProperty").addSimpleField("subSimpleProperty", DataType.STRING)
				.addDynamicObjectField("subSubComplexProperty").addSimpleField("subSubSimpleProperty", DataType.STRING);

		final DefinedMapMetaDataModel metaDataModel = container.build();
		final List<MetaDataField> fields = metaDataModel.getFields();
		Assert.assertThat(fields.size(), is(2));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("simpleProperty").getDataType(), is(DataType.STRING));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("subListComplexProperty").getDataType(), is(DataType.LIST));

		final DefinedMapMetaDataModel subComplexProperty = (DefinedMapMetaDataModel) ((ListMetaDataModel) metaDataModel.getValueMetaDataModel("subListComplexProperty"))
				.getElementModel();
		Assert.assertThat(subComplexProperty.getFields().size(), is(2));

		Assert.assertThat(subComplexProperty.getValueMetaDataModel("subSimpleProperty").getDataType(), is(DataType.STRING));
		Assert.assertThat(subComplexProperty.getValueMetaDataModel("subSubComplexProperty").getDataType(), is(DataType.MAP));

		final DefinedMapMetaDataModel subSubComplexProperty = (DefinedMapMetaDataModel) subComplexProperty.getValueMetaDataModel("subSubComplexProperty");

		Assert.assertThat(subSubComplexProperty.getFields().size(), is(1));
		Assert.assertThat(subSubComplexProperty.getValueMetaDataModel("subSubSimpleProperty").getDataType(), is(DataType.STRING));

	}

	@Test
	public void whenCreatingComplexStructureFieldsShouldBeCreatedCorrectly() {

		final DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
		container.addSimpleField("simpleProperty", DataType.STRING).addListOfDynamicObjectField("subListComplexProperty").addSimpleField("subSimpleProperty", DataType.STRING)
				.addDynamicObjectField("subSubComplexProperty").addSimpleField("subSubSimpleProperty", DataType.STRING).endDynamicObject()
				.addSimpleField("subSimpleProperty2", DataType.STRING).endDynamicObject();

		final DefinedMapMetaDataModel metaDataModel = container.build();
		final List<MetaDataField> fields = metaDataModel.getFields();
		Assert.assertThat(fields.size(), is(2));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("simpleProperty").getDataType(), is(DataType.STRING));
		Assert.assertThat(metaDataModel.getValueMetaDataModel("subListComplexProperty").getDataType(), is(DataType.LIST));

		final DefinedMapMetaDataModel subComplexProperty = (DefinedMapMetaDataModel) ((ListMetaDataModel) metaDataModel.getValueMetaDataModel("subListComplexProperty"))
				.getElementModel();
		Assert.assertThat(subComplexProperty.getFields().size(), is(3));

		Assert.assertThat(subComplexProperty.getValueMetaDataModel("subSimpleProperty").getDataType(), is(DataType.STRING));
		Assert.assertThat(subComplexProperty.getValueMetaDataModel("subSubComplexProperty").getDataType(), is(DataType.MAP));
		Assert.assertThat(subComplexProperty.getValueMetaDataModel("subSimpleProperty2").getDataType(), is(DataType.STRING));

		final DefinedMapMetaDataModel subSubComplexProperty = (DefinedMapMetaDataModel) subComplexProperty.getValueMetaDataModel("subSubComplexProperty");

		Assert.assertThat(subSubComplexProperty.getFields().size(), is(1));
		Assert.assertThat(subSubComplexProperty.getValueMetaDataModel("subSubSimpleProperty").getDataType(), is(DataType.STRING));

	}

	@Test
	public void whenCreatingMapDefaultImplClassShouldBeSet() {
		final DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
		DefinedMapMetaDataModel model = container.build();
		Assert.assertThat(model.getImplementationClass(), is("java.util.HashMap"));
	}

	@Test
	public void whenCreatingListDefaultImplClassShouldBeSet() {
		final ListMetaDataBuilder<?> container = new DefaultMetaDataBuilder().createList();
		container.ofPojo(Object.class);
		ListMetaDataModel model = container.build();
		Assert.assertThat(model.getImplementationClass(), is("java.util.ArrayList"));
	}

	@Test
	public void whenCreatingFieldsDefaultPropertiesShouldBeCreated() {
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.STRING, "java.lang.String");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.BOOLEAN, "java.lang.Boolean");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.BYTE, "java.lang.Byte");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.DATE, "java.util.Date");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.DATE_TIME, "java.util.Calendar");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.ENUM, "java.lang.Enum");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.NUMBER, "java.lang.Number");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.STREAM, "java.io.InputStream");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.STRING, "java.lang.String");
		parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType.VOID, "java.lang.Void");
	}

	private void parameterizedWhenCreatingFieldsDefaultPropertiesShouldBeCreated(DataType dataType, String expectedImplClassName) {
		DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
		container.addSimpleField("simpleField", dataType).endDynamicObject();
		DefinedMapMetaDataModel metaDataModel = container.build();

		Assert.assertThat(metaDataModel.getFields().size(), is(1));
		MetaDataField firstField = metaDataModel.getFields().get(0);
		Assert.assertThat(firstField.getName(), is("simpleField"));
		Assert.assertTrue(firstField.hasProperty(DsqlSelectMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlOrderMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlWhereMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlQueryOperatorsMetaDataFieldProperty.class));

		DsqlQueryOperatorsMetaDataFieldProperty supportedOperatorsProperty = firstField.getProperty(DsqlQueryOperatorsMetaDataFieldProperty.class);
		Assert.assertThat(supportedOperatorsProperty.getSupportedOperators(), equalTo(SupportedOperatorsFactory.getInstance().getSupportedOperationsFor(dataType)));

		Assert.assertThat(firstField.getMetaDataModel().getImplementationClass(), is(expectedImplClassName));
	}

	@Test
	public void whenSpecifyingPropertiesAlsoTheDefaultsShouldBeCreated() {
		DefinedMapMetaDataModel metaDataModel = new DefaultMetaDataBuilder().createDynamicObject("Container").addSimpleField("simpleField", DataType.STRING).isWhereCapable(true).build();


		Assert.assertThat(metaDataModel.getFields().size(), is(1));
		MetaDataField firstField = metaDataModel.getFields().get(0);
		Assert.assertThat(firstField.getName(), is("simpleField"));
		Assert.assertTrue(firstField.hasProperty(DsqlWhereMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlSelectMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlOrderMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlQueryOperatorsMetaDataFieldProperty.class));
	}

	@Test
	public void whenSpecifyingOperatorsOnlyThoseShouldBeCreated() {
		DynamicObjectBuilder<?> container = new DefaultMetaDataBuilder().createDynamicObject("Container");
		container.addSimpleField("simpleField", DataType.STRING).isWhereCapable(true).withSpecificOperations().supportsEquals().supportsLike();
		DefinedMapMetaDataModel metaDataModel = container.build();

		Assert.assertThat(metaDataModel.getFields().size(), is(1));
		MetaDataField firstField = metaDataModel.getFields().get(0);
		Assert.assertThat(firstField.getName(), is("simpleField"));
		Assert.assertTrue(firstField.hasProperty(DsqlWhereMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlQueryOperatorsMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlSelectMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlOrderMetaDataFieldProperty.class));

		DsqlQueryOperatorsMetaDataFieldProperty property = firstField.getProperty(DsqlQueryOperatorsMetaDataFieldProperty.class);
		List<Operator> supportedOperators = property.getSupportedOperators();

		Assert.assertThat(supportedOperators.size(), is(2));
		Assert.assertTrue(supportedOperators.contains(new EqualsOperator()));
		Assert.assertTrue(supportedOperators.contains(new LikeOperator()));
	}

	@Test
	public void whenSpecifyingAFieldIsNotCapableOfSomethingThePropertyShouldNotBeCreatedButTheRestOfTheDefaultsShould() {
		DefinedMapMetaDataModel metaDataModel = new DefaultMetaDataBuilder().createDynamicObject("Container") //
				.addSimpleField("simpleField", DataType.STRING) //
				.isWhereCapable(false) //
				.build();

		Assert.assertThat(metaDataModel.getFields().size(), is(1));
		MetaDataField firstField = metaDataModel.getFields().get(0);
		Assert.assertThat(firstField.getName(), is("simpleField"));
		Assert.assertFalse(firstField.hasProperty(DsqlWhereMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlQueryOperatorsMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlSelectMetaDataFieldProperty.class));
		Assert.assertTrue(firstField.hasProperty(DsqlOrderMetaDataFieldProperty.class));
	}

	@Test
	public void testListOfPojos() {
		ListMetaDataModel result = new DefaultMetaDataBuilder().createList().ofPojo(EverythingPojo.class).endPojo().build();
        Assert.assertThat( result.getElementModel(),  isExactlyA(DefaultPojoMetaDataModel.class));
		Assert.assertThat(((PojoMetaDataModel) result.getElementModel()).getFields().size(), is(17));
	}

    @Test
    public void testListOfMetadataModels(){
        DefinedMapMetaDataModel family = new DefaultMetaDataBuilder().createDynamicObject("Family")
                .addSimpleField("Name", DataType.STRING)
                .addListOfDynamicObjectField("Members")
                .addSimpleField("Name", DataType.STRING).endDynamicObject()
                .addSimpleField("Address", DataType.STRING).build();
        Assert.assertThat(family.getFields().size(), is(3));
    }

    @Test
    public void testListOfSimpleField(){
        DefinedMapMetaDataModel family = new DefaultMetaDataBuilder().createDynamicObject("Family")
                .addSimpleField("Name", DataType.STRING)
                .addList("addresses").ofSimpleField(DataType.STRING).build();
        Assert.assertThat(family.getFields().size(), is(2));
        MetaDataField metaDataField = family.getFields().get(1);
        Assert.assertThat(metaDataField.getMetaDataModel().getDataType(), is(DataType.LIST));
        Assert.assertThat(((ListMetaDataModel)metaDataField.getMetaDataModel()).getElementModel().getDataType(), is(DataType.STRING));
    }

	@Test
	public void compilationTests(){
		DefinedMapMetaDataModel metaDataModel = new DefaultMetaDataBuilder().createDynamicObject("Container") //
				.addSimpleField("simpleField", DataType.STRING) //
				.isSelectCapable(true) //
				.isOrderByCapable(true) //
				.isWhereCapable(false) //
				.build();

		DefinedMapMetaDataModel metaDataModel2 = new DefaultMetaDataBuilder().createDynamicObject("Container") //
				.addDynamicObjectField("nestedDynamicObject")
				.addSimpleField("simpleField", DataType.STRING) //
				.isSelectCapable(true) //
				.isOrderByCapable(true) //
				.isWhereCapable(false) //
				.endDynamicObject() //
				.addSimpleField("anotherSimpleField", DataType.STRING) //
				.build();
	}


    @Test
    public void whenSettingLabelAndDescriptionWithBuilderTheyShouldBeSet(){
        DefinedMapMetaDataModel user = new DefaultMetaDataBuilder().createDynamicObject("User")
                .addSimpleField("name", DataType.STRING)
                .setLabel("Name")
                .setDescription("The name")
                .build();
        MetaDataField metaDataField = user.getFields().get(0);
        Assert.assertThat(metaDataField.getProperty(LabelMetaDataProperty.class).getLabel(), CoreMatchers.is("Name"));
        Assert.assertThat(metaDataField.getProperty(DescriptionMetaDataProperty.class).getDescription(), CoreMatchers.is("The name"));
    }

}
