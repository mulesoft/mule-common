package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class OpeningParenthesesDsqlNode extends DsqlNode {

    public OpeningParenthesesDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}
