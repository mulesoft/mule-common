package org.mule.common.query.expression;

public abstract class BaseOperator implements Operator {

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }

}
