package org.mule.common.metadata.datatype;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

public class DataTypeFactory {

    private interface MetaDataModelEvaluator<T> {
        public boolean isEvaluatable(Class<T> c);
        public DataType evaluate(Class<T> c);
    }

    private static class AssignableMetaDataModelEvaluator<T> implements MetaDataModelEvaluator<T> {

        private Class<?>[] parentClasses;
        private DataType dataType;

        public AssignableMetaDataModelEvaluator(Class<?> parentClass, DataType dataType) {
            this(new Class[] {parentClass}, dataType);
        }
        public AssignableMetaDataModelEvaluator(Class<?>[] parentClasses, DataType dataType) {
            this.parentClasses = parentClasses;
            this.dataType = dataType;
        }

        public DataType evaluate(Class<T> c) {
            if (isEvaluatable(c)) {
                return dataType;
            }
            return null;
        }

        public boolean isEvaluatable(Class<T> c) {
            for (Class<?> parentClass : parentClasses) {
                if (parentClass.isAssignableFrom(c)) {
                    return true;
                }
            }
            return false;
        }
        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    private static class EnumMetaDataModelEvaluator extends AssignableMetaDataModelEvaluator<Enumeration<?>>
    {
        public EnumMetaDataModelEvaluator()
        {
            super(Enumeration.class, DataType.ENUM);
        }

        @Override
        public boolean isEvaluatable(Class<Enumeration<?>> c)
        {
            return c.isEnum() || super.isEvaluatable(c);
        }
    }

    private static final MetaDataModelEvaluator<Void> VOID_EVALUATOR = new AssignableMetaDataModelEvaluator<Void>(
            new Class[] {void.class, Void.class}, DataType.VOID);

    private static final MetaDataModelEvaluator<Boolean> BOOLEAN_EVALUATOR = new AssignableMetaDataModelEvaluator<Boolean>(
            new Class[] {boolean.class, Boolean.class}, DataType.BOOLEAN);

    private static final MetaDataModelEvaluator<String> STRING_EVALUATOR = new AssignableMetaDataModelEvaluator<String>(
            new Class[] {String.class, char.class, Character.class}, DataType.STRING);

    private static final MetaDataModelEvaluator<Number> NUMBER_EVALUATOR = new AssignableMetaDataModelEvaluator<Number>(
            new Class[] {int.class, long.class, short.class ,double.class, float.class, Number.class}, DataType.NUMBER);

    private static final MetaDataModelEvaluator<Number> INTEGER_EVALUATOR = new AssignableMetaDataModelEvaluator<Number>(
            new Class[] {int.class, long.class, short.class}, DataType.INTEGER);

    private static final MetaDataModelEvaluator<Number> DOUBLE_EVALUATOR = new AssignableMetaDataModelEvaluator<Number>(
            new Class[] {double.class, float.class}, DataType.DOUBLE);

    private static final MetaDataModelEvaluator<Number> LONG_EVALUATOR = new AssignableMetaDataModelEvaluator<Number>(
            new Class[] {long.class}, DataType.LONG);

    private static final MetaDataModelEvaluator<Number> DECIMAL_EVALUATOR = new AssignableMetaDataModelEvaluator<Number>(
            new Class[] {BigDecimal.class, BigInteger.class}, DataType.DECIMAL);

    private static final MetaDataModelEvaluator<Byte> BYTE_EVALUATOR = new AssignableMetaDataModelEvaluator<Byte>(
            new Class[] {byte.class, Byte.class}, DataType.BYTE);

    private static final MetaDataModelEvaluator<?> DATE_TIME_EVALUATOR = new AssignableMetaDataModelEvaluator<Object>(
            new Class[] {Calendar.class, XMLGregorianCalendar.class}, DataType.DATE_TIME); // || DateTime.class.isAssignableFrom(c);

    private static final MetaDataModelEvaluator<?> DATE_EVALUATOR = new AssignableMetaDataModelEvaluator<Object>(
            new Class[] {Date.class, java.sql.Date.class}, DataType.DATE); // || DateTime.class.isAssignableFrom(c);

    private static final MetaDataModelEvaluator<?> STREAM_EVALUATOR = new AssignableMetaDataModelEvaluator<Object>(
            new Class[] {InputStream.class, OutputStream.class, Reader.class, Writer.class}, DataType.STREAM);

    private static final MetaDataModelEvaluator<Enumeration<?>> ENUM_EVALUATOR = new EnumMetaDataModelEvaluator();

    private static final MetaDataModelEvaluator<List<?>> LIST_EVALUATOR = new AssignableMetaDataModelEvaluator<List<?>>(
            new Class[] {List.class, Set.class, Object[].class}, DataType.LIST);

    private static final MetaDataModelEvaluator<Map<?, ?>> MAP_EVALUATOR = new AssignableMetaDataModelEvaluator<Map<?, ?>>(
            Map.class, DataType.MAP);

    private static final MetaDataModelEvaluator<?> POJO_EVALUATOR = new AssignableMetaDataModelEvaluator<Object>(
            Object.class, DataType.POJO) {
        @Override
        public boolean isEvaluatable(Class<Object> c) {
            return !c.isPrimitive();
        }
    };

    private static final MetaDataModelEvaluator<?>[] evaluators = new MetaDataModelEvaluator[] {
        VOID_EVALUATOR,
        BOOLEAN_EVALUATOR,
        STRING_EVALUATOR,
        INTEGER_EVALUATOR,
        DOUBLE_EVALUATOR,
        LONG_EVALUATOR,
        DECIMAL_EVALUATOR,
        NUMBER_EVALUATOR,
        BYTE_EVALUATOR,
        DATE_EVALUATOR,
        DATE_TIME_EVALUATOR,
        STREAM_EVALUATOR,
        ENUM_EVALUATOR,
        LIST_EVALUATOR,
        MAP_EVALUATOR,
        POJO_EVALUATOR
    };

    private static DataTypeFactory instance = new DataTypeFactory();

    private DataTypeFactory() {}

    public static DataTypeFactory getInstance() {
        return instance;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataType getDataType(Class<?> clazz) {
        DataType dataType = null;
        for (MetaDataModelEvaluator e : evaluators) {
            if (e.isEvaluatable(clazz)) {
                dataType = e.evaluate(clazz);
                if (dataType != null){
                    break;
                }
            }
        }
        if(dataType == null) {
            throw new RuntimeException("Data Type for class "+clazz.getName()+" could not be found.");
        }
        return dataType;
    }
}
