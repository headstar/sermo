package com.headstartech.sermo;

/**
 * Exception thrown when there is an error in a service supporting the {@link DialogExecutor}.
 *
 */
public class DialogServiceException extends DialogException {

    private static final long serialVersionUID = -600976618369746559L;

    public DialogServiceException(Throwable cause) {
        super(cause);
    }

    public DialogServiceException(String message) {
        super(message);
    }

    public DialogServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
