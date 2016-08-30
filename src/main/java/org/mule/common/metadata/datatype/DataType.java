package org.mule.common.metadata.datatype;

import org.mule.common.query.expression.Operator;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public enum DataType
{
    @Deprecated
    VOID(Void.class.getName()), //TODO to be removed
    UNKNOWN(Object.class.getName()),
    BOOLEAN(Boolean.class.getName()),
    @Deprecated
    NUMBER(Number.class.getName()),
    INTEGER(Integer.class.getName()),
    DOUBLE(Double.class.getName()),
    DECIMAL(BigDecimal.class.getName()),
    FLOAT(Float.class.getName()),
    STRING(String.class.getName()),
    SHORT(Short.class.getName()),
    LONG(Long.class.getName()),
    BYTE(Byte.class.getName()),
    @Deprecated
    STREAM(InputStream.class.getName()), //TODO to be removed
    ENUM(Enum.class.getName()),
    DATE(Date.class.getName()),
    DATE_TIME(Calendar.class.getName()),
    POJO(Object.class.getName()),
    LIST(ArrayList.class.getName()),
    MAP(HashMap.class.getName()),
    XML(null),
    CSV(null),
    JSON(null),
    FLATFILE(null),
    EXCEL(null)
    // TODO: how do we model a UNION type (e.j. Object or Exception)
    ;

    private String defaultImplementationClass;

	private DataType(String defaultImplementationClass) {
		this.defaultImplementationClass = defaultImplementationClass;
	}

	public List<Operator> getSupportedOperators() {
		return SupportedOperatorsFactory.getInstance().getSupportedOperationsFor(this);
	}

	public String getDefaultImplementationClass() {
		return defaultImplementationClass;
	}
}
