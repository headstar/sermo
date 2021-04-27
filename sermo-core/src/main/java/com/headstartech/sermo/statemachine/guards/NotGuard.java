package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

/**
 * A {@link Guard} that represents the logical negation of the supplied guard.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public class NotGuard<S, E extends DialogEvent> implements Guard<S,E> {

    private final Guard<S, E> delegate;

    public NotGuard(Guard<S, E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean evaluate(StateContext<S, E> context) {
        return !delegate.evaluate(context);
    }
}
