package org.mule.common.query.expression;

/**
 * an integer value
 */
public class NumberValue extends Value<Number>
{

    public NumberValue(Number value)
    {
        super(value);
    }

    public static NumberValue fromLiteral(String literal)
    {
        return new NumberValue(Double.parseDouble(literal));
    }
}
