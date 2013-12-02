package org.mule.common.query.expression;

/**
 * Represents a binary operator for a field
 */

public interface Operator {

    String accept(OperatorVisitor operatorVisitor);
}
