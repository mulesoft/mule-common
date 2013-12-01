package org.mule.common.query.expression;

/**
 * Represents an string value
 */
public class StringValue extends Value<String>
{

    public StringValue(String value)
    {
        super(value);
    }

    public static StringValue fromLiteral(String literal)
    {
        String value = literal;
        if (literal.startsWith("\'") && literal.endsWith("\'"))
        {
          value =  value.substring(1, literal.length() - 1);
        }
        return new StringValue(value);
    }

    public String getValueWrappedWith(char wrap)
    {
        return wrap + getValue() + wrap;
    }

    public String toString()
    {
        return "'" + getValue() + "'";
    }

}
