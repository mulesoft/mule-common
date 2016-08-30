package org.mule.common.metadata.datatype;


import org.mule.common.query.expression.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SupportedOperatorsFactory {

    private static SupportedOperatorsFactory instance = new SupportedOperatorsFactory();

    private SupportedOperatorsFactory(){

    }

    public static SupportedOperatorsFactory getInstance(){
        return instance;
    }

    public List<Operator> getSupportedOperationsFor(DataType dataType){
        switch (dataType) {
            case BOOLEAN:
            case ENUM:
                return Arrays.<Operator>asList(new EqualsOperator(), new NotEqualsOperator());
            case DATE_TIME:
            case DATE:
            case BYTE:
            case NUMBER:
            case INTEGER:
            case LONG:
            case DOUBLE:
            case DECIMAL:
                return getCommonOperations();
            case STRING:
                List<Operator> operators =  getCommonOperations();
                operators.add(new LikeOperator());
                return operators;
            case VOID:
            case STREAM:
            case POJO:
            case LIST:
            case MAP:
            case XML:
            case CSV:
            case JSON:
            case FLATFILE:
            case EXCEL:
            default:
                return new ArrayList<Operator>();
        }
    }

    public List<Operator> getCommonOperations(){
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new LessOperator());
        operators.add(new LessOrEqualsOperator());
        operators.add(new EqualsOperator());
        operators.add(new GreaterOperator());
        operators.add(new GreaterOrEqualsOperator());
        operators.add(new NotEqualsOperator());

        return operators;
    }

}
