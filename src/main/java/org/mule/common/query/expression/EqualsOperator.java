package org.mule.common.query.expression;

/**
 * Represents an equal operator
 */
public class EqualsOperator extends AbstractBinaryOperator {

    public String accept(OperatorVisitor operatorVisitor) {
        return operatorVisitor.equalsOperator();
    }
}
