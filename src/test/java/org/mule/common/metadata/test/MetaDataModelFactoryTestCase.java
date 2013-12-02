/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.common.metadata.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.mule.common.metadata.ListMetaDataModel;
import org.mule.common.metadata.MetaDataField;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.MetaDataModelFactory;
import org.mule.common.metadata.ParameterizedMapMetaDataModel;
import org.mule.common.metadata.PojoMetaDataModel;
import org.mule.common.metadata.SimpleMetaDataModel;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.field.property.DefaultFieldPropertyFactory;
import org.mule.common.metadata.field.property.ValidStringValuesFieldProperty;

import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MetaDataModelFactoryTestCase
{

    public interface S {}

    public interface S1 extends S {}

    public interface S2 extends S1, S {}

    public class DuplicateInterfaceObject implements S2, S {
    }

    public class Container {
        private S2 dup = new DuplicateInterfaceObject();
        public S2 getDup()
        {
            return dup;
        }
    }

    public class ContainerOtherGetter {
        private DuplicateInterfaceObject dup = new DuplicateInterfaceObject();
        public DuplicateInterfaceObject getOther() {
            return dup;
        }
    }

    public class ContainerNoGetter {
        public Integer obj = new Integer(101);
    }

    public enum EnumTest {
        ENUM_FIELD1,
        ENUM_FIELD2("EnumValue2"),
        ENUM_FIELD3("EnumValue3");

        private String string;

        EnumTest() {}

        EnumTest(String value) {
            this.string = value;
        }
    }

    public class ContainerWithEnum {
        public EnumTest enumField1 = EnumTest.ENUM_FIELD1;
        public EnumTest enumField2 = EnumTest.ENUM_FIELD2;
        public int i = 102;
        public EnumTest getEnumField1() {
            return enumField1;
        }
        public void setEnumField1(EnumTest enumField1) {
            this.enumField1 = enumField1;
        }
        public EnumTest getEnumField2() {
            return enumField2;
        }
        public void setEnumField2(EnumTest enumField2) {
            this.enumField2 = enumField2;
        }
        public int getI() {
            return i;
        }
        public void setI(int i) {
            this.i = i;
        }
    }

    @Test
    public void testDuplicateInterfaces()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        
        List<MetaDataField> fields = factory.getFieldsForClass(Container.class, new DefaultFieldPropertyFactory());
        assertEquals(1, fields.size());
        MetaDataField dupField = fields.get(0);
        assertTrue(dupField.getProperties().size() == 4);
        assertEquals("org.mule.common.metadata.test.MetaDataModelFactoryTestCase$S2",
                dupField.getMetaDataModel().getImplementationClass());
        assertEquals("dup", dupField.getName());
        MetaDataModel dupm = dupField.getMetaDataModel();
        assertTrue(dupm instanceof PojoMetaDataModel);
        PojoMetaDataModel dupModel = dupm.as(PojoMetaDataModel.class);
        assertEquals(0, dupModel.getFields().size());
        assertEquals(S2.class.getName(), dupModel.getClassName());
        Set<String> parents = dupModel.getParentNames();
        assertEquals(2, parents.size());
        assertTrue(parents.contains(S.class.getName()));
        assertTrue(parents.contains(S1.class.getName()));
    }

    @Test
    public void testEnumFields()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        
        List<MetaDataField> fields = factory.getFieldsForClass(ContainerWithEnum.class, new DefaultFieldPropertyFactory());
        assertEquals(3, fields.size());

        MetaDataField enumField1 = fields.get(0);
        assertTrue(enumField1.getProperties().size() == 5);
        assertEquals("enumField1", enumField1.getName());
        MetaDataModel enumModel1 = enumField1.getMetaDataModel();
        assertTrue(enumModel1 instanceof SimpleMetaDataModel);
        SimpleMetaDataModel simpleEnumModel1 = enumModel1.as(SimpleMetaDataModel.class);
        assertEquals(simpleEnumModel1.getDataType(), DataType.ENUM);
        final ValidStringValuesFieldProperty enumProperty1 = enumField1.getProperty(ValidStringValuesFieldProperty.class);
        assertNotNull(enumProperty1);
        final List<String> validStrings1 = enumProperty1.getValidStrings();
        assertTrue(validStrings1.contains("ENUM_FIELD1"));
        assertTrue(validStrings1.contains("ENUM_FIELD2"));
        assertTrue(validStrings1.contains("ENUM_FIELD3"));
        
        final MetaDataField enumField2 = fields.get(1);
        assertTrue(enumField1.getProperties().size() == 5);
        assertEquals("enumField2", enumField2.getName());
        MetaDataModel enumModel2 = enumField2.getMetaDataModel();
        assertTrue(enumModel2 instanceof SimpleMetaDataModel);
        SimpleMetaDataModel simpleEnumModel2 = enumModel2.as(SimpleMetaDataModel.class);
        assertEquals(simpleEnumModel2.getDataType(), DataType.ENUM);
        final ValidStringValuesFieldProperty enumProperty2 = enumField2.getProperty(ValidStringValuesFieldProperty.class);
        assertNotNull(enumProperty2);
        final List<String> validStrings2 = enumProperty2.getValidStrings();
        assertTrue(validStrings2.contains("ENUM_FIELD1"));
        assertTrue(validStrings2.contains("ENUM_FIELD2"));
        assertTrue(validStrings2.contains("ENUM_FIELD3"));
    }

    @Test
    public void testOddNamedGetter()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        
        List<MetaDataField> fields = factory.getFieldsForClass(ContainerOtherGetter.class, new DefaultFieldPropertyFactory());
        assertEquals(1, fields.size());
        MetaDataField dupField = fields.get(0);
        assertTrue(dupField.getProperties().size() == 4);
        assertEquals("org.mule.common.metadata.test.MetaDataModelFactoryTestCase$DuplicateInterfaceObject",
                dupField.getMetaDataModel().getImplementationClass());
        assertEquals("other", dupField.getName());
        MetaDataModel dupm = dupField.getMetaDataModel();
        assertTrue(dupm instanceof PojoMetaDataModel);
        PojoMetaDataModel dupModel = dupm.as(PojoMetaDataModel.class);
        assertEquals(0, dupModel.getFields().size());
        assertEquals(DuplicateInterfaceObject.class.getName(), dupModel.getClassName());
        Set<String> parents = dupModel.getParentNames();
        assertEquals(4, parents.size());
        assertTrue(parents.contains(S.class.getName()));
        assertTrue(parents.contains(S1.class.getName()));
        assertTrue(parents.contains(S2.class.getName()));
        assertTrue(parents.contains(Object.class.getName()));
    }

    @Test
    public void testNoGetter()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        
        List<MetaDataField> fields = factory.getFieldsForClass(ContainerNoGetter.class, new DefaultFieldPropertyFactory());
        assertEquals(0, fields.size());
    }

    @Test
    public void testGetMetaDataModelForPojoFields()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        
        List<MetaDataField> fieldsForStruct = factory.getFieldsForClass(Struct1.class, new DefaultFieldPropertyFactory());
        assertNotNull(fieldsForStruct);
        
        assertPojoMetaDataModelField(getField(fieldsForStruct, "date"), "date", DataType.DATE);
        assertPojoMetaDataModelField(getField(fieldsForStruct, "value"), "value", DataType.INTEGER);
        MetaDataField field = getField(fieldsForStruct, "byteArray");
        assertTrue(field.getProperties().size() == 4);
        assertNotNull(field);
        MetaDataModel metaDataModel = field.getMetaDataModel();
        assertNotNull(metaDataModel);
        assertListMetaDataModelField(field, "byteArray", DataType.BYTE, true);
    }

    @Test
    public void testGetMetaDataModelForListPojoFields()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();

        List<MetaDataField> fieldsForStruct = factory.getFieldsForClass(ListOfStruct.class, new DefaultFieldPropertyFactory());
        assertNotNull(fieldsForStruct);

        MetaDataField structField = getField(fieldsForStruct, "structList");
        assertTrue(structField.getProperties().size() == 4);
        assertListMetaDataModelField(structField, "structList", DataType.POJO);
        MetaDataModel struct = ((ListMetaDataModel)structField.getMetaDataModel()).getElementModel();
        assertTrue(struct instanceof PojoMetaDataModel);
        assertNotNull(struct.toString());
        PojoMetaDataModel structPojo = (PojoMetaDataModel)struct;
        assertTrue(structPojo.getClassName().equals(Struct.class.getName()));
        assertNotNull(structPojo.toString());
        assertPojoMetaDataModelField(getField(structPojo.getFields(), "date"), "date", DataType.DATE);
        assertPojoMetaDataModelField(getField(structPojo.getFields(), "value"), "value", DataType.INTEGER);
    }

    @Test
    public void testGetMetaDataModelForMapFields()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        
        List<MetaDataField> fieldsForMapStruct = factory.getFieldsForClass(MapOfStruct.class, new DefaultFieldPropertyFactory());
        assertNotNull(fieldsForMapStruct);
        
        MetaDataField structField = getField(fieldsForMapStruct, "structMap");
        assertTrue(structField.getProperties().size() == 4);
        assertParamMapMetaDataModelField(structField, "structMap", DataType.POJO, DataType.POJO);
        
        MetaDataModel keyStruct = ((ParameterizedMapMetaDataModel)structField.getMetaDataModel()).getKeyMetaDataModel();
        MetaDataModel valueStruct = ((ParameterizedMapMetaDataModel)structField.getMetaDataModel()).getValueMetaDataModel();
        
        assertTrue(keyStruct instanceof PojoMetaDataModel);
        assertTrue(valueStruct instanceof PojoMetaDataModel);
        
        PojoMetaDataModel keyStructPojo = (PojoMetaDataModel)keyStruct;
        PojoMetaDataModel valueStructPojo = (PojoMetaDataModel)valueStruct;
        
        assertTrue(keyStructPojo.getClassName().equals(Struct.class.getName()));
        assertTrue(valueStructPojo.getClassName().equals(Struct1.class.getName()));
        
        assertPojoMetaDataModelField(getField(keyStructPojo.getFields(), "date"), "date", DataType.DATE);
        assertPojoMetaDataModelField(getField(keyStructPojo.getFields(), "value"), "value", DataType.INTEGER);
        
        assertPojoMetaDataModelField(getField(valueStructPojo.getFields(), "date"), "date", DataType.DATE);
        assertPojoMetaDataModelField(getField(valueStructPojo.getFields(), "name"), "name", DataType.STRING);
        assertPojoMetaDataModelField(getField(valueStructPojo.getFields(), "value"), "value", DataType.INTEGER);
    }

    @Test
    public void testGetMetaDataModelForRecursivePojos()
    {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        {
            List<MetaDataField> fieldsForNode = factory.getFieldsForClass(Node.class, new DefaultFieldPropertyFactory());
            assertNotNull(fieldsForNode);

            MetaDataField leftField = getField(fieldsForNode, "left");
            assertPojoMetaDataModelField(leftField, "left", DataType.POJO);
            assertPojoMetaDataModelField(getField(fieldsForNode, "right"), "right", DataType.POJO);
            assertPojoMetaDataModelField(getField(fieldsForNode, "value"), "value", DataType.INTEGER);
            assertPojoMetaDataModelField(getField(fieldsForNode, "defaultVisibility"), "defaultVisibility", DataType.INTEGER);
            
            MetaDataModel leftMetaDataModel = leftField.getMetaDataModel();
            assertTrue(leftMetaDataModel instanceof PojoMetaDataModel);
            PojoMetaDataModel leftPojoMetaDataModel = leftMetaDataModel.as(PojoMetaDataModel.class);
            assertTrue(leftPojoMetaDataModel.getClassName().equals(Node.class.getName()));
            List<MetaDataField> leftNodeFields = leftPojoMetaDataModel.getFields();
            
            assertPojoMetaDataModelField(getField(leftNodeFields, "left"), "left", DataType.POJO);
            assertPojoMetaDataModelField(getField(leftNodeFields, "right"), "right", DataType.POJO);
            assertPojoMetaDataModelField(getField(leftNodeFields, "value"), "value", DataType.INTEGER);
            assertPojoMetaDataModelField(getField(leftNodeFields, "defaultVisibility"), "defaultVisibility", DataType.INTEGER);
        }
        {
            List<MetaDataField> fieldsForNodeListOfStruct = factory.getFieldsForClass(NodeListOfStruct.class, new DefaultFieldPropertyFactory());
            assertNotNull(fieldsForNodeListOfStruct);

            MetaDataField structListField = getField(fieldsForNodeListOfStruct,  "structList");
            MetaDataField recursiveListField = getField(fieldsForNodeListOfStruct,  "recursiveNode");
            assertPojoListMetaDataModelField(structListField, "structList", Struct.class);
            
            String name = NodeListOfStruct.class.getName();
            PojoMetaDataModel recursiveNode = (PojoMetaDataModel)recursiveListField.getMetaDataModel();
            assertNodeListOfStruct(recursiveNode, name);
            recursiveNode = (PojoMetaDataModel)getField(recursiveNode.getFields(), "recursiveNode").getMetaDataModel();
            assertNodeListOfStruct(recursiveNode, name);
        }
    }

    private void assertNodeListOfStruct(PojoMetaDataModel pojoModel, String expectedName)
    {
        assertNotNull(pojoModel.toString());
        assertEquals(NodeListOfStruct.class.getName(), pojoModel.getClassName());
        assertEquals(expectedName, pojoModel.getClassName());
        assertNotNull(pojoModel.getParentNames());
        assertTrue(pojoModel.getParentNames().contains("java.lang.Object"));
        assertNotNull(pojoModel.getFields());

        assertListMetaDataModelField(getField(pojoModel.getFields(), "structList"), "structList", DataType.POJO);
        assertPojoMetaDataModelField(getField(pojoModel.getFields(), "recursiveNode"), "recursiveNode", DataType.POJO);

        ListMetaDataModel structList = (ListMetaDataModel)getField(pojoModel.getFields(), "structList").getMetaDataModel();
        assertNotNull(structList.toString());
        MetaDataModel struct = structList.getElementModel();
        assertTrue(struct instanceof PojoMetaDataModel);
        PojoMetaDataModel structPojo = (PojoMetaDataModel)struct;
        assertNotNull(struct.toString());
        assertPojoMetaDataModelField(getField(structPojo.getFields(), "date"), "date", DataType.DATE);
        assertPojoMetaDataModelField(getField(structPojo.getFields(), "value"), "value", DataType.INTEGER);
    }

    public static class ListOfStruct
    {
        private List<Struct> structList;

        public List<Struct> getStructList() {
            return structList;
        }

        public void setStructList(List<Struct> structList) {
            this.structList = structList;
        }
    }

    public static class MapOfStruct
    {
        private Map<Struct, Struct1> structMap;

        public Map<Struct, Struct1> getStructMap() {
            return structMap;
        }

        public void setStructMap(Map<Struct, Struct1> structMap) {
            this.structMap = structMap;
        }
    }

    public static class Struct
    {
        Date date;
        long value = 30940L;
        
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public long getValue() {
            return value;
        }
        public void setValue(long value) {
            this.value = value;
        }
    }

    public static class Struct1
    {
        Date date;
        String name;
        long value = 30941L;
        byte[] byteArray = new byte[12];
        
        public byte[] getByteArray() {
            return byteArray;
        }

        public void setByteArray(byte[] byteArray) {
            this.byteArray = byteArray;
        }

        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public long getValue() {
            return value;
        }
        public void setValue(long value) {
            this.value = value;
        }
    }

    public static class Node
    {
        public Node left;
        protected Node right;
        private int value;
        int defaultVisibility;
        public Node getLeft() {
            return left;
        }
        public void setLeft(Node left) {
            this.left = left;
        }
        public Node getRight() {
            return right;
        }
        public void setRight(Node right) {
            this.right = right;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
        public int getDefaultVisibility() {
            return defaultVisibility;
        }
        public void setDefaultVisibility(int defaultVisibility) {
            this.defaultVisibility = defaultVisibility;
        }
    }

    public static class NodeListOfStruct
    {
        private List<Struct> structList;
        private NodeListOfStruct recursiveNode;
        public List<Struct> getStructList() {
            return structList;
        }
        public void setStructList(List<Struct> structList) {
            this.structList = structList;
        }
        public NodeListOfStruct getRecursiveNode() {
            return recursiveNode;
        }
        public void setRecursiveNode(NodeListOfStruct recursiveNode) {
            this.recursiveNode = recursiveNode;
        }
    }

    private void assertListMetaDataModelField(MetaDataField field, String name, DataType elementDataType)
    {
        assertNotNull(field);
        assertEquals(name, field.getName());
        assertTrue(field.getProperties().size() == 4);
        assertEquals("java.util.ArrayList",
                field.getMetaDataModel().getImplementationClass());

        MetaDataModel model = field.getMetaDataModel();
        assertTrue(model instanceof ListMetaDataModel);
        assertSame(DataType.LIST, model.getDataType());
        ListMetaDataModel listModel = model.as(ListMetaDataModel.class);
        assertNotNull(listModel.getElementModel());
    }

    private void assertListMetaDataModelField(MetaDataField field, String name, DataType elementDataType, boolean isArray)
    {
        assertNotNull(field);
        assertEquals(name, field.getName());
        assertTrue(field.getProperties().size() == 4);
        assertEquals("java.util.ArrayList", field.getMetaDataModel().getImplementationClass());

        MetaDataModel model = field.getMetaDataModel();
        assertTrue(model instanceof ListMetaDataModel);
        assertSame(DataType.LIST, model.getDataType());
        ListMetaDataModel listModel = model.as(ListMetaDataModel.class);
        assertNotNull(listModel.getElementModel());
        assertEquals(isArray, listModel.isArray());
    }

    private void assertPojoListMetaDataModelField(MetaDataField field, String name, Class<?> clazz)
    {
        assertNotNull(field);
        assertEquals(name, field.getName());
        assertTrue(field.getProperties().size() == 4);
        assertEquals("java.util.ArrayList", field.getMetaDataModel().getImplementationClass());

        MetaDataModel model = field.getMetaDataModel();
        assertTrue(model instanceof ListMetaDataModel);
        assertSame(DataType.LIST, model.getDataType());
        ListMetaDataModel listModel = model.as(ListMetaDataModel.class);
        assertNotNull(listModel.getElementModel());
        assertNotNull(listModel.getElementModel().getDataType().equals(DataType.POJO));
        PojoMetaDataModel listPojoModel = listModel.getElementModel().as(PojoMetaDataModel.class);
        assertEquals(listPojoModel.getClassName(), clazz.getName());
    }

    private void assertParamMapMetaDataModelField(MetaDataField field, String name, DataType keyDataType, DataType valueDataType)
    {
        assertNotNull(field);
        assertEquals(name, field.getName());
        assertTrue(field.getProperties().size() == 4);
        assertEquals("java.util.HashMap", field.getMetaDataModel().getImplementationClass());
        
        MetaDataModel model = field.getMetaDataModel();
        
        assertTrue(model instanceof ParameterizedMapMetaDataModel);
        assertSame(DataType.MAP, model.getDataType());
        
        ParameterizedMapMetaDataModel mapModel = model.as(ParameterizedMapMetaDataModel.class);
        assertNotNull(mapModel.getKeyMetaDataModel());
        assertNotNull(mapModel.getValueMetaDataModel());
        
        assertEquals(keyDataType, mapModel.getKeyMetaDataModel().getDataType());
        assertEquals(valueDataType, mapModel.getValueMetaDataModel().getDataType());
    }

    private MetaDataField getField(List<MetaDataField> fields, String name)
    {
        if (fields != null)
        {
            for (MetaDataField mdf : fields)
            {
                if (name.equals(mdf.getName()))
                {
                    return mdf;
                }
            }
        }
        return null;
    }

    private void assertPojoMetaDataModelField(MetaDataField field, String name, DataType dt)
    {
        assertNotNull(field);
        assertTrue(field.getProperties().size() == 4);
        assertEquals(getDefaultImplementationClass(field, dt),
                field.getMetaDataModel().getImplementationClass());
        assertEquals(name, field.getName());
        
        MetaDataModel model = field.getMetaDataModel();
        assertFalse(model instanceof ListMetaDataModel);
        assertFalse(model instanceof ParameterizedMapMetaDataModel);
        assertEquals(dt, model.getDataType());
    }

    public static class SocketMapContainer {
        Map<String, Socket> socketMap = new HashMap<String, Socket>();
        public Map<String, Socket> getSocketMap() {
            return socketMap;
        }
        public Socket getSocket(String id) {
            return socketMap.get(id);
        }
    }

    @Test
    public void testTwoGetters() {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        List<MetaDataField> fields = factory.getFieldsForClass(SocketMapContainer.class, new DefaultFieldPropertyFactory());
        assertEquals(1, fields.size());
        for (MetaDataField f : fields)
        {
            if ("socketMap".equals(f.getName()))
            {
                ParameterizedMapMetaDataModel socketMapModel = f.getMetaDataModel().as(ParameterizedMapMetaDataModel.class);
                assertSame(DataType.MAP, socketMapModel.getDataType());
                assertEquals(null, socketMapModel.getName());
                MetaDataModel keyMetaDataModel = socketMapModel.getKeyMetaDataModel();
                assertEquals(DataType.STRING, keyMetaDataModel.getDataType());
                assertTrue(keyMetaDataModel instanceof SimpleMetaDataModel);
                MetaDataModel valueMetaDataModel = socketMapModel.getValueMetaDataModel();
                assertEquals(DataType.POJO, valueMetaDataModel.getDataType());
                assertEquals(Socket.class.getName(), valueMetaDataModel.as(PojoMetaDataModel.class).getClassName());
            }
            else
            {
                fail("Shouldn't have any other fields. Unexpected field name: " + f.getName());
            }
        }
    }

    public static class MapLikeContainer {
        private Map<String, String> properties = new HashMap<String, String>();
        public void put(String key, String value) {
            properties.put(key, value);
        }
        public String get(String id) {
            return properties.get(id);
        }
    }

    @Test
    public void testMapLikeContainer() {
        MetaDataModelFactory factory = MetaDataModelFactory.getInstance();
        List<MetaDataField> fields = factory.getFieldsForClass(MapLikeContainer.class, new DefaultFieldPropertyFactory());
        // doesn't have a bean like interface so no fields are found.
        assertEquals(0, fields.size());
    }

    private String getDefaultImplementationClass(MetaDataField field, DataType dt) {
        switch (dt) {
            case BOOLEAN:
                return "java.lang.Boolean";
            case ENUM:
                return "java.lang.Enum";
            case DATE:
                return "java.util.Date";
            case DATE_TIME:
                return "java.util.Calendar";
            case BYTE:
                return "java.lang.Byte";
            case NUMBER:
                return "java.lang.Number";
            case INTEGER:
                return "java.lang.Integer";
            case DOUBLE:
                return "java.lang.Double";
            case DECIMAL:
                return "java.math.BigDecimal";
            case STRING:
                return "java.lang.String";
            case VOID:
                return "java.lang.Void";
            case STREAM:
                return "java.io.InputStream";
            case POJO:
                return field.getMetaDataModel().getImplementationClass();
            default:
                return null;
        }
    }
}
