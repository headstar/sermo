package com.headstartech.sermo;

/**
 * Exception wrapping exception thrown when there is an error loading/persisting the dialog state.
 *
 */
public class DialogPersisterException extends RuntimeException {

    private static final long serialVersionUID = -600976618369746559L;

    public DialogPersisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
