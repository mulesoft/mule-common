package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class NotDsqlNode extends DsqlNode {

    public NotDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}
