package org.mule.common.query;

import org.mule.common.query.expression.Direction;
import org.mule.common.query.expression.EmptyExpression;
import org.mule.common.query.expression.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the query model for Mule DsqlQuery Builder
 *
 * @author Mulesoft, Inc
 */
public class DsqlQuery implements Query{

    /**
     * Type or types to be queried
     */
    protected List<Type> types;

    /**
     * Fields to be retrieved
     */
    protected List<Field> fields;

    /**
     * Fields for sorting the query
     */
    protected List<Field> orderByFields;

    /**
     * Direction to determine the ascending or descending sorting
     */
    protected Direction direction;

    /**
     * Expression which contains the filter conditions
     */
    protected Expression filterExpression;

    /**
     * Expression for joining the different types
     */
    protected Expression joinExpression;

    protected int limit;

    protected int offset;


    public DsqlQuery(){
        this.types = new ArrayList<Type>();
        this.fields = new ArrayList<Field>();
        this.orderByFields = new ArrayList<Field>();
        this.direction = Direction.ASC;
        this.filterExpression = new EmptyExpression();
        this.joinExpression = new EmptyExpression();
        this.limit = -1;
        this.offset = -1;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Field> getOrderByFields() {
        return orderByFields;
    }

    public Direction getDirection(){
        return direction;
    }

    public Expression getFilterExpression() {
        return filterExpression;
    }

    public List<Type> getTypes() {
        return types;
    }

    public Expression getJoinExpression() {
        return joinExpression;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public void addType(Type type) {
        this.types.add(type);
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public void addOrderField(Field orderByField) {
        this.orderByFields.add(orderByField);
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public void setFilterExpression(Expression filterExpression) {
        this.filterExpression = filterExpression;
    }

    public void setJoinExpression(Expression joinExpression) {
        this.joinExpression = joinExpression;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void accept(QueryVisitor queryVisitor) {
        // This order matters! Please don't change it. Visit types first, then fields.
        queryVisitor.visitTypes(this.types);
        queryVisitor.visitFields(this.fields);
        if (!(this.filterExpression instanceof EmptyExpression)) {
            queryVisitor.visitBeginExpression();
            this.filterExpression.accept(queryVisitor);
        }

        if (this.orderByFields.size()>0){
            queryVisitor.visitOrderByFields(this.orderByFields, this.direction);
        }

        if (limit != -1) {
            queryVisitor.visitLimit(this.limit);
        }

        if (offset != -1) {
            queryVisitor.visitOffset(this.offset);
        }
    }

    public boolean hasDirection() {
        return this.direction != null;
    }
}
