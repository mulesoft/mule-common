package org.mule.common.query.dsql.parser;

import org.mule.common.query.expression.EqualsOperator;
import org.mule.common.query.expression.GreaterOperator;
import org.mule.common.query.expression.GreaterOrEqualsOperator;
import org.mule.common.query.expression.LessOperator;
import org.mule.common.query.expression.LessOrEqualsOperator;
import org.mule.common.query.expression.LikeOperator;
import org.mule.common.query.expression.NotEqualsOperator;
import org.mule.common.query.expression.Operator;

import java.util.HashMap;

public final class QueryModelOperatorFactory {

    private static QueryModelOperatorFactory instance = new QueryModelOperatorFactory();
    
    private HashMap<String, Operator> operators = new HashMap<String, Operator>();
    
    private QueryModelOperatorFactory() {
        operators.put("=", new EqualsOperator());
        operators.put(">", new GreaterOperator());
        operators.put("<", new LessOperator());
        operators.put(">=", new GreaterOrEqualsOperator());
        operators.put("<=", new LessOrEqualsOperator());
        operators.put("<>", new NotEqualsOperator());

    }
    
    public static QueryModelOperatorFactory getInstance() {
        return instance;
    }
    
    public Operator getOperator(String symbol) {
        if(symbol.equalsIgnoreCase("like")){
            return new LikeOperator();
        }
        return operators.get(symbol);
    }
}
