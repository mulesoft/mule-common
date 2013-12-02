package org.mule.common.query.dsql.parser;

import org.antlr.runtime.Token;

public class OperatorDsqlNode extends DsqlNode {

    public OperatorDsqlNode(Token t) {
        super(t);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        visitor.visit(this);
    }
}
