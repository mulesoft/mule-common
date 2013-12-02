package org.mule.common.query.expression;

/**
 * Represents an less or equals operator
 */
public class LessOrEqualsOperator extends AbstractBinaryOperator{

    public String accept(OperatorVisitor operatorVisitor) {
        return operatorVisitor.lessOrEqualsOperator();
    }
}
