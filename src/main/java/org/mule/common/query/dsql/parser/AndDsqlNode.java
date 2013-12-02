package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class AndDsqlNode extends DsqlNode {

    public AndDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}
