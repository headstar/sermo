package com.headstartech.sermo;

/**
 * Exception thrown when there is an error in a service supporting the {@link SermoDialogExecutor}.
 *
 */
public class SermoDialogServiceException extends SermoDialogException {

    private static final long serialVersionUID = -600976618369746559L;

    public SermoDialogServiceException(Throwable cause) {
        super(cause);
    }

    public SermoDialogServiceException(String message) {
        super(message);
    }

    public SermoDialogServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
