package org.mule.common.query;

import org.mule.common.query.expression.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for query visitor
 */
public class DsqlQueryVisitorTest
{  
    private static final List<String> KEYWORDS = Arrays.asList("ASC", "DESC", "SELECT", "FROM", "WHERE", "ORDER", "BY", "LIMIT", "OFFSET", "AND", "OR", "NOT", "LIKE");

    @Test
    public void ensureAllKeywordsAreTested() 
    {
        Assert.assertThat(KEYWORDS, IsCollectionWithSize.hasSize(DsqlKeyword.values().length));
        for (DsqlKeyword keyword : DsqlKeyword.values()) 
        {
            Assert.assertThat(KEYWORDS, IsCollectionContaining.hasItem(keyword.toString()));
        }
    }

    @Test
    public void whenUsingKeywordsAsFieldsTheyShouldBeQuoted() 
    {
        for (String keyword : getRandomlyCapitalizedKeywords()) 
        {
            QueryBuilder queryBuilder = new DefaultQueryBuilder();
            queryBuilder.addField(new Field("name", "string"));
            queryBuilder.addField(new Field(keyword, "string"));
            queryBuilder.addType(new Type("Account"));

            DsqlQueryVisitor visitor = new DsqlQueryVisitor();
            try 
            {
                queryBuilder.build().accept(visitor);
            } 
            catch (QueryBuilderException e) 
            {
            }
            Assert.assertEquals("SELECT name,'" + keyword + "' FROM Account", visitor.dsqlQuery());
        }
    }

    @Test
    public void whenUsingKeywordsInWhereTheyShouldBeQuoted() 
    {
        for (String keyword : getRandomlyCapitalizedKeywords()) 
        {
            QueryBuilder queryBuilder = new DefaultQueryBuilder();
            queryBuilder.addField(new Field("name", "string"));
            Field keywordField = new Field(keyword, "string");
            queryBuilder.addField(keywordField);
            queryBuilder.addType(new Type("Account"));
            Expression newExp = new FieldComparation(new GreaterOperator(), keywordField, new StringValue("foo"));
            queryBuilder.setFilterExpression(newExp);

            DsqlQueryVisitor visitor = new DsqlQueryVisitor();
            try 
            {
                queryBuilder.build().accept(visitor);
            } 
            catch (QueryBuilderException e) 
            {
            }
            Assert.assertEquals("SELECT name,'" + keyword + "' FROM Account WHERE '" + keyword + "' > 'foo'", visitor.dsqlQuery());
        }
    }

    @Test
    public void whenUsingKeywordsAsTypesTheyShouldBeQuoted() 
    {
        for (String keyword : getRandomlyCapitalizedKeywords()) 
        {
            QueryBuilder queryBuilder = new DefaultQueryBuilder();
            queryBuilder.addField(new Field("name", "string"));
            queryBuilder.addField(new Field("lastName", "string"));
            queryBuilder.addType(new Type(keyword));

            DsqlQueryVisitor visitor = new DsqlQueryVisitor();
            try 
            {
                queryBuilder.build().accept(visitor);
            }
            catch (QueryBuilderException e) 
            {
            }
            Assert.assertEquals("SELECT name,lastName FROM '" + keyword + "'", visitor.dsqlQuery());
        }
    }

    private List<String> getRandomlyCapitalizedKeywords() 
    {
        List<String> result = new LinkedList<String>();
        for (String keyword : KEYWORDS) 
        {
            if (RandomUtils.nextBoolean()) 
            {
                String capitalized = "";
                for (int i = 0; i < keyword.length(); i++) 
                {
                    char nextChar = keyword.charAt(i);
                    capitalized += RandomUtils.nextBoolean() ? Character.toUpperCase(nextChar) : Character.toLowerCase(nextChar);
                }
                result.add(capitalized);
            } 
            else 
            {
                result.add(keyword);
            }
        }
        return result;
    }

    @Test
    public void whenUsingKeywordsInOrderByShouldBeQuoted() 
    {
        for (String keyword : KEYWORDS) 
        {
            QueryBuilder queryBuilder = new DefaultQueryBuilder();
            queryBuilder.addField(new Field("name", "string"));
            Field keywordField = new Field(keyword, "string");
            queryBuilder.addField(keywordField);
            queryBuilder.addType(new Type("Account"));
            queryBuilder.addOrderByField(keywordField);

            DsqlQueryVisitor visitor = new DsqlQueryVisitor();
            try 
            {
                queryBuilder.build().accept(visitor);
            }
            catch (QueryBuilderException e) 
            {
            }
            Assert.assertEquals("SELECT name,'" + keyword + "' FROM Account ORDER BY '" + keyword + "' ASC", visitor.dsqlQuery());
        }
    }
    
    @Test
    public void testBasicQueryVisitor()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addField(new Field("lastName", "string"));
        queryBuilder.addType(new Type("Account"));

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name,lastName FROM Account", visitor.dsqlQuery());
    }

    @Test
    public void testFiltersQueryVisitor()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addType(new Type("Account"));
        Expression comparision = new FieldComparation(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
        Expression anotherComparision = new FieldComparation(new GreaterOperator(), new Field("grade", "int"), new IntegerValue(0));
        Expression simpleAnd = new And(comparision, anotherComparision);
        queryBuilder.setFilterExpression(simpleAnd);

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name FROM Account WHERE (age < 18 AND grade > 0)", visitor.dsqlQuery());

    }


    @Test
    public void testFiltersQueryVisitorWithDouble()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addType(new Type("Account"));
        Expression comparision = new FieldComparation(new LessOperator(), new Field("age", "int"), new NumberValue(18.5));
        Expression anotherComparision = new FieldComparation(new GreaterOperator(), new Field("grade", "int"), new NumberValue(0.5));
        Expression simpleAnd = new And(comparision, anotherComparision);
        queryBuilder.setFilterExpression(simpleAnd);

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name FROM Account WHERE (age < 18.5 AND grade > 0.5)", visitor.dsqlQuery());

    }

    @Test
    public void testPrecedence()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addType(new Type("Account"));
        Expression comparision = new FieldComparation(new NotEqualsOperator(), new Field("age", "int"), new IntegerValue(18));
        Expression anotherComparision = new FieldComparation(new GreaterOperator(), new Field("grade", "int"), new IntegerValue(0));
        Expression simpleOr = new Or(comparision, anotherComparision);
        Expression simpleAnd = new And(simpleOr, anotherComparision);
        queryBuilder.setFilterExpression(simpleAnd);

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name FROM Account WHERE ((age <> 18 OR grade > 0) AND grade > 0)", visitor.dsqlQuery());
    }

    @Test
    public void testOrderBy()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addType(new Type("Account"));
        queryBuilder.addOrderByField(new Field("name", "string"));
        queryBuilder.addOrderByField(new Field("age", "int"));
        queryBuilder.setDirection(Direction.ASC);
        Expression comparision = new FieldComparation(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
        Expression anotherComparision = new FieldComparation(new EqualsOperator(), new Field("grade", "int"), new IntegerValue(0));
        Expression simpleOr = new Or(comparision, anotherComparision);
        Expression simpleAnd = new And(simpleOr, anotherComparision);
        queryBuilder.setFilterExpression(simpleAnd);

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name FROM Account WHERE ((age < 18 OR grade = 0) AND grade = 0) ORDER BY name,age ASC", visitor.dsqlQuery());
    }

    @Test
    public void testLimitAndOffset()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addType(new Type("Account"));
        queryBuilder.addOrderByField(new Field("name", "string"));
        queryBuilder.addOrderByField(new Field("age", "int"));
        queryBuilder.setDirection(Direction.DESC);
        queryBuilder.setLimit(10);
        queryBuilder.setOffset(20);
        Expression comparision = new FieldComparation(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
        Expression anotherComparision = new FieldComparation(new GreaterOperator(), new Field("grade", "int"), new IntegerValue(0));
        Expression simpleOr = new Or(comparision, anotherComparision);
        Expression simpleAnd = new And(simpleOr, anotherComparision);
        queryBuilder.setFilterExpression(simpleAnd);

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name FROM Account WHERE ((age < 18 OR grade > 0) AND grade > 0) ORDER BY name,age DESC LIMIT 10 OFFSET 20", visitor.dsqlQuery());
    }

    @Test
    public void testIdentifierValue()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addType(new Type("Account"));
        queryBuilder.addOrderByField(new Field("name", "string"));
        queryBuilder.addOrderByField(new Field("age", "int"));
        queryBuilder.setDirection(Direction.ASC);
        queryBuilder.setLimit(10);
        queryBuilder.setOffset(20);
        Expression comparision = new FieldComparation(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
        Expression anotherComparision = new FieldComparation(new GreaterOperator(), new Field("grade", "int"), IdentifierValue.fromLiteral("NEXT_WEEK"));
        Expression simpleOr = new Or(comparision, anotherComparision);
        Expression simpleAnd = new And(simpleOr, anotherComparision);
        queryBuilder.setFilterExpression(simpleAnd);

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name FROM Account WHERE ((age < 18 OR grade > NEXT_WEEK) AND grade > NEXT_WEEK) ORDER BY name,age ASC LIMIT 10 OFFSET 20", visitor.dsqlQuery());
    }


    @Test
    public void testStringValue()
    {
        QueryBuilder queryBuilder = new DefaultQueryBuilder();
        queryBuilder.addField(new Field("name", "string"));
        queryBuilder.addType(new Type("Account"));
        queryBuilder.addOrderByField(new Field("name", "string"));
        queryBuilder.addOrderByField(new Field("age", "int"));
        queryBuilder.setDirection(Direction.ASC);
        queryBuilder.setLimit(10);
        queryBuilder.setOffset(20);
        Expression comparision = new FieldComparation(new LessOperator(), new Field("age", "int"), new StringValue("old"));
        Expression anotherComparision = new FieldComparation(new GreaterOperator(), new Field("grade", "int"), IdentifierValue.fromLiteral("NEXT_WEEK"));
        Expression simpleOr = new Or(comparision, anotherComparision);
        Expression simpleAnd = new And(simpleOr, anotherComparision);
        queryBuilder.setFilterExpression(simpleAnd);

        DsqlQueryVisitor visitor = new DsqlQueryVisitor();
        try
        {
            queryBuilder.build().accept(visitor);
        }
        catch (QueryBuilderException e)
        {

        }
        Assert.assertEquals("SELECT name FROM Account WHERE ((age < 'old' OR grade > NEXT_WEEK) AND grade > NEXT_WEEK) ORDER BY name,age ASC LIMIT 10 OFFSET 20", visitor.dsqlQuery());
    }
}
