package org.mule.common.query.dsql.parser;

import org.mule.common.query.DefaultQueryBuilder;
import org.mule.common.query.Field;
import org.mule.common.query.QueryBuilder;
import org.mule.common.query.Type;
import org.mule.common.query.dsql.grammar.DsqlParser;
import org.mule.common.query.expression.*;

import java.util.List;
import java.util.Stack;

public class DefaultDsqlGrammarVisitor implements DsqlGrammarVisitor {

    private QueryBuilder queryBuilder;
    private Stack<Expression> expressions = new Stack<Expression>();
    private int expressionLevel;

    public DefaultDsqlGrammarVisitor() {
        queryBuilder = new DefaultQueryBuilder();
        expressionLevel = 0;
    }

    public void visit(DsqlNode dsqlNode) {
        // Too generic. Empty on purpose.
    }

    public void visit(SelectDsqlNode selectDsqlNode) {
        List<IDsqlNode> children = selectDsqlNode.getChildren();

        for (final IDsqlNode dsqlNode : children) {
            if (dsqlNode.getType() != DsqlParser.IDENT
                    && dsqlNode.getType() != DsqlParser.STRING_LITERAL
                    && dsqlNode.getType() != DsqlParser.ASTERIX) {
                dsqlNode.accept(this);
            } else {
                String nodeText = dsqlNode.getText();
                if (dsqlNode.getType() == DsqlParser.STRING_LITERAL) {
                    nodeText = StringValue.fromLiteral(nodeText).getValue();
                }
                queryBuilder.addField(new Field(nodeText, "string"));
            }
        }
    }

    public void visit(FromDsqlNode fromDsqlNode) {
        List<IDsqlNode> children = fromDsqlNode.getChildren();

        for (final IDsqlNode dsqlNode : children) {
            String text = getTextIfStringLiteral(dsqlNode);
            queryBuilder.addType(new Type(text));
        }
    }

    private String getTextIfStringLiteral(IDsqlNode dsqlNode) {
        String text = dsqlNode.getText();
        if (dsqlNode.getType() == DsqlParser.STRING_LITERAL)
            return StringValue.fromLiteral(text).getValue();
        else {
            return text;
        }
    }

    public void visit(ExpressionDsqlNode expressionDsqlNode) {
        List<IDsqlNode> children = expressionDsqlNode.getChildren();

        for (final IDsqlNode dsqlNode : children) {
            int type = dsqlNode.getType();
            if (type == DsqlParser.AND || type == DsqlParser.OR
                    || type == DsqlParser.NOT) {
                dsqlNode.accept(this);
            } else if (type == DsqlParser.OPERATOR || type == DsqlParser.COMPARATOR) {
                final List<IDsqlNode> operatorChildren = dsqlNode.getChildren();
                IDsqlNode fieldNode = operatorChildren.get(0);
                String fieldName = getTextIfStringLiteral(fieldNode);
                final Field field = new Field(fieldName);
                final IDsqlNode valueNode = operatorChildren.get(1);
                final Value value = buildValue(valueNode);
                final FieldComparation expression = new FieldComparation(
                        getOperatorFor(dsqlNode.getText()), field, value);
                queryBuilder.setFilterExpression(expression);
            } else if (type == DsqlParser.OPENING_PARENTHESIS) {
                dsqlNode.accept(this);
            }
        }
    }

    private Value buildValue(IDsqlNode node) {
        Value value;
        switch (node.getType()) {
            case DsqlParser.NUMBER_LITERAL :
                value = NumberValue.fromLiteral(node.getText());
                break;
            case DsqlParser.BOOLEAN_LITERAL :
                value = BooleanValue.fromLiteral(node.getText());
                break;
            case DsqlParser.DATE_LITERAL :
                value = DateValue.fromLiteral(node.getText());
                break;
            case DsqlParser.DATE_TIME_LITERAL :
                value = DateTimeValue.fromLiteral(node.getText());
                break;
            case DsqlParser.NULL_LITERAL :
                value = new NullValue();
                break;
            case DsqlParser.IDENT :
                value = IdentifierValue.fromLiteral(node.getText());
                break;
            case DsqlParser.MULE_EXPRESSION :
                value = MuleExpressionValue.fromLiteral(node.getText());
                break;
            case DsqlParser.STRING_LITERAL :
                value = StringValue.fromLiteral(node.getText());
                break;
            default :
                value = UnknownValue.fromLiteral(node.getText());
                break;
        }
        return value;
    }

    public void visit(AndDsqlNode andDsqlNode) {
        List<IDsqlNode> children = andDsqlNode.getChildren();
        expressionLevel++;
        for (IDsqlNode dsqlNode : children) {
            dsqlNode.accept(this);
        }
        expressionLevel--;
        Expression rightExpression = expressions.pop();
        Expression leftExpression = expressions.pop();
        And andExpression = new And(leftExpression, rightExpression);
        putExpression(andExpression);
    }

    private void putExpression(Expression expression) {
        if (expressionLevel == 0) {
            queryBuilder.setFilterExpression(expression);
        } else {
            expressions.push(expression);
        }
    }

    public void visit(OrDsqlNode orDsqlNode) {
        List<IDsqlNode> children = orDsqlNode.getChildren();
        expressionLevel++;
        for (IDsqlNode dsqlNode : children) {
            dsqlNode.accept(this);
        }
        expressionLevel--;
        Expression rightExpression = expressions.pop();
        Expression leftExpression = expressions.pop();
        putExpression(new Or(leftExpression, rightExpression));
    }

    public void visit(NotDsqlNode notDsqlNode) {
        List<IDsqlNode> children = notDsqlNode.getChildren();
        expressionLevel++;
        for (IDsqlNode dsqlNode : children) {
            dsqlNode.accept(this);
        }
        expressionLevel--;
        Expression expression = expressions.pop();
        putExpression(new Not(expression));
    }

    public void visit(OperatorDsqlNode operatorDsqlNode) {
        List<IDsqlNode> children = operatorDsqlNode.getChildren();
        IDsqlNode fieldNode = children.get(0);
        Field field = new Field(getTextIfStringLiteral(fieldNode));
        IDsqlNode dsqlNode = children.get(1);
        Value<?> value = buildValue(dsqlNode);
        putExpression(new FieldComparation(getOperatorFor(operatorDsqlNode
                .getText()), field, value));
    }

    public void visit(OpeningParenthesesDsqlNode openingParenthesesDsqlNode) {
        List<IDsqlNode> children = openingParenthesesDsqlNode.getChildren();

        for (IDsqlNode dsqlNode : children) {
            dsqlNode.accept(this);
        }
    }

    private BinaryOperator getOperatorFor(String symbol) {
        // TODO: refactor this when we start using unary operators.
        return (BinaryOperator) QueryModelOperatorFactory.getInstance()
                .getOperator(symbol);
    }

    public void visit(OrderByDsqlNode orderByDsqlNode) {
        List<IDsqlNode> children = orderByDsqlNode.getChildren();

        for (final IDsqlNode dsqlNode : children) {
            String text = getTextIfStringLiteral(dsqlNode);
            if (! (dsqlNode instanceof DirectionDsqlNode) ){
                queryBuilder.addOrderByField(new Field(text));
            }else {
                queryBuilder.setDirection(QueryModelDirectionFactory.getInstance().getDirection(text.toLowerCase()));
            }
        }
    }

    public void visit(LimitDsqlNode limitDsqlNode) {
        List<IDsqlNode> children = limitDsqlNode.getChildren();

        for (final IDsqlNode dsqlNode : children) {
            queryBuilder.setLimit(Integer.parseInt(dsqlNode.getText()));
        }
    }

    public void visit(OffsetDsqlNode offsetDsqlNode) {
        List<IDsqlNode> children = offsetDsqlNode.getChildren();

        for (final IDsqlNode dsqlNode : children) {
            queryBuilder.setOffset(Integer.parseInt(dsqlNode.getText()));
        }
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }
}
