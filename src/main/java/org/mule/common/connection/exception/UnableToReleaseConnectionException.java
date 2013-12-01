
package org.mule.common.connection.exception;

/**
 * Exception thrown when the release connection operation of the connection manager
 * fails.
 */
public class UnableToReleaseConnectionException extends Exception
{

    private static final long serialVersionUID = 7535502322086523521L;

    /**
     * Create a new exception
     * 
     * @param throwable Inner exception
     */
    public UnableToReleaseConnectionException(Throwable throwable)
    {
        super(throwable);
    }

}
