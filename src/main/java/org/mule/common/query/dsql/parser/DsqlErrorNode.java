package org.mule.common.query.dsql.parser;

import org.mule.common.query.dsql.parser.exception.DsqlParsingException;

import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonErrorNode;

public class DsqlErrorNode 
    extends CommonErrorNode 
    implements IDsqlNode {

    public DsqlErrorNode(TokenStream input, Token start, Token stop, RecognitionException e) {
        super(input, start, stop, e);
    }

    public void accept(DsqlGrammarVisitor visitor) {
        throw new DsqlParsingException();
    }

    @SuppressWarnings("unchecked")
    public List<IDsqlNode> getChildren() {
        return (List<IDsqlNode>) super.getChildren();
    }

}