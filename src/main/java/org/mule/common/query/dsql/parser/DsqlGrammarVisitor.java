package org.mule.common.query.dsql.parser;

public interface DsqlGrammarVisitor {

    void visit(DsqlNode dsqlNode);

    void visit(SelectDsqlNode selectDsqlNode);

    void visit(FromDsqlNode fromDsqlNode);

    void visit(ExpressionDsqlNode selectDsqlNode);
    
    void visit(AndDsqlNode andDsqlNode);
    
    void visit(OrDsqlNode orDsqlNode);

    void visit(NotDsqlNode notDsqlNode);
    
    void visit(OpeningParenthesesDsqlNode openingParenthesesDsqlNode);
    
    void visit(OperatorDsqlNode operatorDsqlNode);

    void visit(OrderByDsqlNode orderByDsqlNode);

    void visit(LimitDsqlNode limitDsqlNode);

    void visit(OffsetDsqlNode offsetDsqlNode);
}