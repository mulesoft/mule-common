/**
 *
 */
package org.mule.common.query.expression;

public class BooleanValue extends Value<Boolean>
{

    public BooleanValue(Boolean value)
    {
        super(value);
    }

    public static BooleanValue fromLiteral(String literal){
        return new BooleanValue(Boolean.parseBoolean(literal));
    }

}
