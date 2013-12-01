package org.mule.common.query.expression;

import org.mule.common.query.DefaultOperatorVisitor;

/**
 */
public abstract class AbstractBinaryOperator extends BaseOperator implements BinaryOperator {

    public String toString() {
        return accept(new DefaultOperatorVisitor());
    }
}
