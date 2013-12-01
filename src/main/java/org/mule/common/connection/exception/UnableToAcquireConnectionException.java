
package org.mule.common.connection.exception;


/**
 * Exception thrown when the connection needed for executing an operation is null.
 */
public class UnableToAcquireConnectionException extends Exception
{

    private static final long serialVersionUID = -6373735896212545766L;

    /**
     * Create a new exception
     */
    public UnableToAcquireConnectionException()
    {
    }

    /**
     * Create a new exception
     */
    public UnableToAcquireConnectionException(String message)
    {
        super(message);
    }

    /**
     * Create a new exception
     * 
     * @param throwable Inner exception
     */
    public UnableToAcquireConnectionException(Throwable throwable)
    {
        super(throwable);
    }

    /**
     * Create a new exception
     * 
     * @param message a custom message
     * @param throwable Inner exception
     */
    public UnableToAcquireConnectionException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
