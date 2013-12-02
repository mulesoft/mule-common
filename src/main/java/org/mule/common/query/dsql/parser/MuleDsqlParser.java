package org.mule.common.query.dsql.parser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.mule.common.query.DsqlQuery;
import org.mule.common.query.QueryBuilder;
import org.mule.common.query.QueryBuilderException;
import org.mule.common.query.dsql.grammar.DsqlLexer;
import org.mule.common.query.dsql.grammar.DsqlParser;
import org.mule.common.query.dsql.grammar.DsqlParser.select_return;
import org.mule.common.query.dsql.parser.exception.DsqlParsingException;

public class MuleDsqlParser {

    public DsqlQuery parse(final String string) {
        String parseString = string;
        if (string.startsWith("dsql:")) {
            parseString = string.substring(5);
        }
        CharStream antlrStringStream = new ANTLRStringStream(parseString);
        DsqlLexer dsqlLexer = new DsqlLexer(antlrStringStream);
        CommonTokenStream dsqlTokens = new CommonTokenStream();
        dsqlTokens.setTokenSource(dsqlLexer);
        
        DsqlParser dsqlParser = new DsqlParser(dsqlTokens);
        dsqlParser.setTreeAdaptor(new DsqlTreeAdaptor());
        return parse(dsqlParser);
    }

    private DsqlQuery parse(DsqlParser dsqlParser) {
        try {
            select_return select = dsqlParser.select();
            DsqlNode tree = (DsqlNode) select.getTree();
            return buildQuery(tree).build();
        } catch (RecognitionException e) {
            throw new DsqlParsingException(e);
        } catch (QueryBuilderException e) {
            throw new DsqlParsingException(e);
        }
    }

    private QueryBuilder buildQuery(DsqlNode dsqlRootNode) {
        DefaultDsqlGrammarVisitor visitor = new DefaultDsqlGrammarVisitor();
        dsqlRootNode.accept(visitor);
        
        return visitor.getQueryBuilder();
    }
    
}
