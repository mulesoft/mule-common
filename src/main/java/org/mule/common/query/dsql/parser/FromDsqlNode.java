package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class FromDsqlNode extends DsqlNode {
    
    public FromDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}
