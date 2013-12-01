package org.mule.common.query.dsql.parser;

import java.util.List;

public interface IDsqlNode {

    public int getType();

    public List<IDsqlNode> getChildren();

    public String getText();

    public void accept(DsqlGrammarVisitor visitor);

}
