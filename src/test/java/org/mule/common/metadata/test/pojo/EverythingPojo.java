package org.mule.common.metadata.test.pojo;

import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

public class EverythingPojo
{
    public enum TestEnum {
        FOO, BAR
    }

    private Date date;
    private Calendar calendar;
    private XMLGregorianCalendar xmlGregorianCalendar;
    private Boolean booleanObject;
    private boolean primitiveBoolean;
    private Integer integer;
    private int primitiveInt;
    private Long longObject;
    private long primitiveLong;
    private Short shortObject;
    private short primitiveShort;
    private Double doubleObject;
    private double primitiveDouble;
    private Byte byteObject;
    private byte primitiveByte;
    private String string;
    private TestEnum testEnum;

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Calendar getCalendar() {
        return calendar;
    }
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
    public XMLGregorianCalendar getXmlGregorianCalendar() {
        return xmlGregorianCalendar;
    }
    public void setXmlGregorianCalendar(XMLGregorianCalendar xmlGregorianCalendar) {
        this.xmlGregorianCalendar = xmlGregorianCalendar;
    }
    public Boolean getBooleanObject() {
        return booleanObject;
    }
    public void setBooleanObject(Boolean booleanObject) {
        this.booleanObject = booleanObject;
    }
    public boolean isPrimitiveBoolean() {
        return primitiveBoolean;
    }
    public void setPrimitiveBoolean(boolean primitiveBoolean) {
        this.primitiveBoolean = primitiveBoolean;
    }
    public Integer getInteger() {
        return integer;
    }
    public void setInteger(Integer integer) {
        this.integer = integer;
    }
    public int getPrimitiveInt() {
        return primitiveInt;
    }
    public void setPrimitiveInt(int primitiveInt) {
        this.primitiveInt = primitiveInt;
    }
    public Long getLongObject() {
        return longObject;
    }
    public void setLongObject(Long longObject) {
        this.longObject = longObject;
    }
    public long getPrimitiveLong() {
        return primitiveLong;
    }
    public void setPrimitiveLong(long primitiveLong) {
        this.primitiveLong = primitiveLong;
    }
    public Short getShortObject() {
        return shortObject;
    }
    public void setShortObject(Short shortObject) {
        this.shortObject = shortObject;
    }
    public short getPrimitiveShort() {
        return primitiveShort;
    }
    public void setPrimitiveShort(short primitiveShort) {
        this.primitiveShort = primitiveShort;
    }
    public Double getDoubleObject() {
        return doubleObject;
    }
    public void setDoubleObject(Double doubleObject) {
        this.doubleObject = doubleObject;
    }
    public double getPrimitiveDouble() {
        return primitiveDouble;
    }
    public void setPrimitiveDouble(double primitiveDouble) {
        this.primitiveDouble = primitiveDouble;
    }
    public Byte getByteObject() {
        return byteObject;
    }
    public void setByteObject(Byte byteObject) {
        this.byteObject = byteObject;
    }
    public byte getPrimitiveByte() {
        return primitiveByte;
    }
    public void setPrimitiveByte(byte primitiveByte) {
        this.primitiveByte = primitiveByte;
    }
    public String getString() {
        return string;
    }
    public void setString(String string) {
        this.string = string;
    }
    public TestEnum getTestEnum() {
        return testEnum;
    }
    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }

}
