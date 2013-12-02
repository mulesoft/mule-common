package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class OrderByDsqlNode extends DsqlNode {
    
    public OrderByDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}