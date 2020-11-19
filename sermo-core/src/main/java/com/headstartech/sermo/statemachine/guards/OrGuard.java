package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.Collection;

/**
 * A composed {@link Guard} that represents a short-circuiting logical OR of the supplied guards.
 *
 * @param <S>
 * @param <E>
 */
public class OrGuard<S, E extends DialogEvent> implements Guard<S,E> {

    private final Collection<Guard<S, E>> delegates;

    public OrGuard(Collection<Guard<S, E>> delegates) {
        this.delegates = delegates;
        if(delegates.size() == 0) {
            throw new IllegalArgumentException("must be at least one Guard");
        }
    }

    @Override
    public boolean evaluate(StateContext<S, E> context) {
        for(Guard<S, E> delegate : delegates) {
            if(delegate.evaluate(context)) {
                return true;
            }
        }
        return false;
    }
}
