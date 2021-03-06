package org.mule.common.query.expression;

/**
 * Represents a less or equals operator
 */
public class GreaterOrEqualsOperator extends AbstractBinaryOperator{
    @Override
    public String accept(OperatorVisitor operatorVisitor) {
        return operatorVisitor.greaterOrEqualsOperator();
    }
}
