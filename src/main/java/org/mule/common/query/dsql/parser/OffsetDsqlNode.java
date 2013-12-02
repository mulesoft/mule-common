package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class OffsetDsqlNode extends DsqlNode {

    public OffsetDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}