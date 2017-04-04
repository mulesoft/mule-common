package org.mule.common.query;

import static org.mule.common.query.DsqlKeyword.*;

import org.mule.common.query.expression.*;

import java.util.Iterator;
import java.util.List;

/**
 * <p>This visitor translate an <strong>DSQL</strong> query represented as {@link DsqlQuery} to its {@code String} representation.</p>
 */
public class DsqlQueryVisitor extends DefaultQueryVisitor
{

    private StringBuilder stringBuilder;

    public DsqlQueryVisitor()
    {
        stringBuilder = new StringBuilder();
    }

    @Override
    public void visitFields(List<Field> fields)
    {
        StringBuilder select = new StringBuilder();
        select.append(SELECT);
        select.append(" ");
        Iterator<Field> fieldIterable = fields.iterator();
        while (fieldIterable.hasNext())
        {
            String fieldName = addQuotesIfNeeded(fieldIterable.next().getName());
			select.append(fieldName);
            if (fieldIterable.hasNext())
            {
                select.append(",");
            }
        }

        stringBuilder.insert(0, select);
    }

    private String addQuotesIfNeeded(String name) 
    {
        String result = name;
        if (name.contains(" ") || isKeywordIgnoreCase(name)) 
        {
            result = addQuotes(name);
        }
        return result;
    }

    private String addQuotes(String typeName) 
    {
        StringBuilder result = new StringBuilder(typeName);
        result.insert(0, '\'');
        result.append('\'');
        return result.toString();
    }

	@Override
    public void visitTypes(List<Type> types)
    {
        stringBuilder.append(" ");
        stringBuilder.append(FROM);
        stringBuilder.append(" ");
        Iterator<Type> typeIterator = types.iterator();
        while (typeIterator.hasNext())
        {
            String typeName = addQuotesIfNeeded(typeIterator.next().getName());
			stringBuilder.append(typeName);
            if (typeIterator.hasNext())
            {
                stringBuilder.append(",");
            }
        }
    }

    @Override
    public void visitOrderByFields(List<Field> orderByFields, Direction direction)
    {
        stringBuilder.append(" ");
        stringBuilder.append(ORDER);
        stringBuilder.append(" ");
        stringBuilder.append(BY);
        stringBuilder.append(" ");
        Iterator<Field> orderByFieldsIterator = orderByFields.iterator();
        while (orderByFieldsIterator.hasNext())
        {
            String fieldName = addQuotesIfNeeded(orderByFieldsIterator.next().getName());
            stringBuilder.append(fieldName);
            if (orderByFieldsIterator.hasNext())
            {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(" ");
        stringBuilder.append(direction.toString());
    }

    @Override
    public void visitBeginExpression()
    {
        stringBuilder.append(" ");
        stringBuilder.append(WHERE);
        stringBuilder.append(" ");
    }

    @Override
    public void visitInitPrecedence()
    {
        stringBuilder.append("(");
    }

    @Override
    public void visitEndPrecedence()
    {
        stringBuilder.append(")");
    }

    @Override
    public void visitLimit(int limit)
    {
        stringBuilder.append(" ");
        stringBuilder.append(LIMIT);
        stringBuilder.append(" ").append(limit);
    }

    @Override
    public void visitOffset(int offset)
    {
        stringBuilder.append(" ");
        stringBuilder.append(OFFSET);
        stringBuilder.append(" ").append(offset);
    }

    @Override
    public void _dont_implement_QueryVisitor___instead_extend_DefaultQueryVisitor()
    {

    }

    @Override
    public void visitAnd()
    {
        stringBuilder.append(" ");
        stringBuilder.append(AND);
        stringBuilder.append(" ");
    }


    @Override
    public void visitOR()
    {
        stringBuilder.append(" ");
        stringBuilder.append(OR);
        stringBuilder.append(" ");
    }

    @Override
    public void visitComparison(String operator, Field field, Value<?> value)
    {
        String name = addQuotesIfNeeded(field.getName());
		stringBuilder.append(name).append(operator).append(value.toString());
    }

    @Override
    public OperatorVisitor operatorVisitor()
    {
        return new DefaultOperatorVisitor();
    }

    public String dsqlQuery()
    {
        return stringBuilder.toString();
    }


}
