package com.headstartech.sermo;

/**
 * Exception thrown when an application throws an exception in an {@link org.springframework.statemachine.action.Action}.
 *
 */
public class SermoDialogExecutionException extends SermoDialogException {

    private static final long serialVersionUID = -6809402600590120302L;

    public SermoDialogExecutionException(Throwable cause) {
        super(cause);
    }

    public SermoDialogExecutionException(String message) {
        super(message);
    }

    public SermoDialogExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
