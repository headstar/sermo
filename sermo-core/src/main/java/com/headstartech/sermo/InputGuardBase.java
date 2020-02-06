package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.function.Function;

/**
 * @author Per Johansson
 */
public abstract class InputGuardBase<S, E> implements Guard<S, E> {

    private final Function<E, String> eventToInput;

    public InputGuardBase(Function<E, String> eventToInput) {
        this.eventToInput = eventToInput;
    }

    @Override
    public boolean evaluate(StateContext<S, E> context) {
        return doEvaluate(context, eventToInput.apply(context.getEvent()));
    }

    protected abstract boolean doEvaluate(StateContext<S, E> context, String input);
}
