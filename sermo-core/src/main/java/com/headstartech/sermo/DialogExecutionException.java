package com.headstartech.sermo;

/**
 * Exception thrown when an application throws an exception in an {@link org.springframework.statemachine.action.Action}.
 *
 */
public class DialogExecutionException extends DialogException {

    private static final long serialVersionUID = -6809402600590120302L;

    public DialogExecutionException(Throwable cause) {
        super(cause);
    }

    public DialogExecutionException(String message) {
        super(message);
    }

    public DialogExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
