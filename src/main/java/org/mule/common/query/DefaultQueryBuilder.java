package org.mule.common.query;

import org.mule.common.query.expression.Expression;

/**
 * Basic Query builder
 */
public class DefaultQueryBuilder extends QueryBuilder {

    private DefaultQuery query;

    public DefaultQueryBuilder() {
        this.query = new DefaultQuery();
    }

    @Override
    public QueryBuilder addType(Type type) {
        this.query.addType(type);
        return this;
    }

    @Override
    public QueryBuilder addField(Field field) {
        this.query.addField(field);
        return this;
    }

    @Override
    public QueryBuilder addOrderByField(Field field) {
        this.query.addOrderField(field);
        return this;
    }

    @Override
    public QueryBuilder setFilterExpression(Expression expression) {
        this.query.setFilterExpression(expression);
        return this;
    }

    @Override
    public QueryBuilder setJoinExpression(Expression joinExpression) {
        this.query.setJoinExpression(joinExpression);
        return this;
    }

    @Override
    public QueryBuilder setLimit(int limit) {
        this.query.setLimit(limit);
        return this;
    }

    @Override
    public QueryBuilder setOffset(int offset) {
        this.query.setOffset(offset);
        return this;
    }

    @Override
    public Query build() throws QueryBuilderException {
        return this.query;
    }
}