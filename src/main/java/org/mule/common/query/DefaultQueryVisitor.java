package org.mule.common.query;

import org.mule.common.query.expression.Direction;
import org.mule.common.query.expression.OperatorVisitor;
import org.mule.common.query.expression.Value;

import java.util.List;

/**
 * Extend this class to create your own query visitor
 */

public abstract class DefaultQueryVisitor implements QueryVisitor {

    public void visitFields(List<Field> fields) {

    }

    public void visitTypes(List<Type> types) {

    }

    public void visitAnd() {

    }

    public void visitOR() {

    }

    public void visitComparison(String operator, Field field, Value<?> value) {

    }

    public OperatorVisitor operatorVisitor() {
        return new DefaultOperatorVisitor();
    }

    public void visitBeginExpression() {

    }

    public void visitInitPrecedence() {

    }

    public void visitEndPrecedence() {

    }

    public void visitLimit(int limit) {

    }

    public void visitOffset(int offset) {

    }

    public void visitOrderByFields(List<Field> orderByFields, Direction direction) {

    }

    public void _dont_implement_QueryVisitor___instead_extend_DefaultQueryVisitor() {
    }
}
