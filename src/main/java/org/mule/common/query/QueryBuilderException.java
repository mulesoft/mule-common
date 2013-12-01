package org.mule.common.query;

/**
 * This exception is throwed if the query can't be constructed based on the current state of the builder.
 */
public class QueryBuilderException extends Exception {

    private static final long serialVersionUID = 6047023569759420365L;

    public QueryBuilderException() {
    }

    public QueryBuilderException(String text) {
        super(text);
    }
}
