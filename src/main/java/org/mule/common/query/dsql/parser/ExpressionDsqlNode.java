package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class ExpressionDsqlNode extends DsqlNode {

    public ExpressionDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}
