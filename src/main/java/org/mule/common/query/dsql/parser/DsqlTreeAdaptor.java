package org.mule.common.query.dsql.parser;

import org.mule.common.query.dsql.grammar.DsqlParser;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;

public class DsqlTreeAdaptor extends CommonTreeAdaptor {

    @Override
    public Object create(Token payload) {
        // TODO: Check if there is a better way to do this.
        Object retVal;
        if (payload != null) {
            switch (payload.getType()) {
            case DsqlParser.SELECT: {
                retVal = new SelectDsqlNode(payload);
                break;
            }
            case DsqlParser.FROM: {
                retVal = new FromDsqlNode(payload);
                break;
            }
            case DsqlParser.WHERE: {
                retVal = new ExpressionDsqlNode(payload);
                break;
            }
            case DsqlParser.AND: {
                retVal = new AndDsqlNode(payload);
                break;
            }
            case DsqlParser.OR: {
                retVal = new OrDsqlNode(payload);
                break;
            }
            case DsqlParser.NOT: {
                retVal = new NotDsqlNode(payload);
                break;
            }
            case DsqlParser.OPENING_PARENTHESIS: {
                retVal = new OpeningParenthesesDsqlNode(payload);
                break;
            }
            case DsqlParser.COMPARATOR:
            case DsqlParser.OPERATOR: {
                retVal = new OperatorDsqlNode(payload);
                break;
            }
            case DsqlParser.ORDER: {
                retVal = new OrderByDsqlNode(payload);
                break;
            }
            case DsqlParser.ASC: {
                retVal = new DirectionDsqlNode(payload);
                break;
            }
            case DsqlParser.DESC: {
                retVal = new DirectionDsqlNode(payload);
                break;
            }
            case DsqlParser.LIMIT: {
                retVal = new LimitDsqlNode(payload);
                break;
            }
            case DsqlParser.OFFSET: {
                retVal = new OffsetDsqlNode(payload);
                break;
            }
            default: {
                retVal = new DsqlNode(payload);
            }
            }
        } else {
            retVal = new DsqlNode(payload);
        }
        return retVal;
    }

    @Override
    public Object errorNode(TokenStream input, Token start, Token stop,
            RecognitionException e) {
        return new DsqlErrorNode(input, start, stop, e);
    }

}
