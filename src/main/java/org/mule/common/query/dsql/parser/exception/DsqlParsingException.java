package org.mule.common.query.dsql.parser.exception;

public class DsqlParsingException extends RuntimeException {
    private static final long serialVersionUID = -6223667924316685779L;

    public DsqlParsingException(Throwable t) {
        super(t);
    }

    public DsqlParsingException(String s)
    {
        super(s);
    }

    public DsqlParsingException()
    {
    }
}
