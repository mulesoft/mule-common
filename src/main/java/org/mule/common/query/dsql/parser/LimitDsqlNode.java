package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class LimitDsqlNode extends DsqlNode {

    public LimitDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}