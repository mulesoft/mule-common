package org.mule.common.query;

import org.mule.common.query.expression.OperatorVisitor;

public class DefaultOperatorVisitor implements OperatorVisitor {
    public static final String LIKE = " like ";
    public static final String GREATER_OR_EQUALS = " >= ";
    public static final String NOT_EQUALS = " <> ";
    public static final String EQUALS = " = ";
    public static final String LESS_OR_EQUALS = " <= ";
    public static final String GREATER = " > ";
    public static final String LESS = " < ";

    public String lessOperator() {
        return LESS;
    }

    public String greaterOperator() {
        return GREATER;
    }

    public String lessOrEqualsOperator() {
        return LESS_OR_EQUALS;
    }

    public String equalsOperator() {
        return EQUALS;
    }

    public String notEqualsOperator() {
        return NOT_EQUALS;
    }

    public String greaterOrEqualsOperator() {
        return GREATER_OR_EQUALS;
    }

    public String likeOperator()
    {
        return LIKE;
    }

    public void _dont_implement_OperatorVisitor___instead_extend_DefaultOperatorVisitor() {

    }
}
