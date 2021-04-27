package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.StateContext;

/**
 * A {@link org.springframework.statemachine.guard.Guard} returning true/false based on whether the user input equals the supplied value or not.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public class InputEqualsGuard<S, E extends DialogEvent> extends GuardBase<S, E> {

    private final String value;

    public InputEqualsGuard(String value) {
        if(value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be empty!");
        }
        this.value = value;
    }

    @Override
    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        return value.equals(input);
    }
}
