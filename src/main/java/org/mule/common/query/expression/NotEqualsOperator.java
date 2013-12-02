package org.mule.common.query.expression;

/**
 * Represents an not equals operator
 */

public class NotEqualsOperator extends AbstractBinaryOperator {

    public String accept(OperatorVisitor operatorVisitor) {
        return operatorVisitor.notEqualsOperator();
    }
}
