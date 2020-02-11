package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Per Johansson
 */
public class FormInputGuard<S, E> extends InputGuardBase<S, E> {

    private final Predicate<String> predicate;

    public FormInputGuard(Function<E, String> eventToInput, Predicate<String> predicate) {
        super(eventToInput);
        this.predicate = predicate;
    }

    @Override
    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        return predicate.test(input);
    }
}
