package com.headstartech.sermo.guards;

import com.headstartech.sermo.MOInput;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.function.Predicate;

/**
 * @author Per Johansson
 */
public class FormInputGuard<S, E extends MOInput> extends GuardBase<S, E> {

    private final Predicate<String> predicate;

    public FormInputGuard(Predicate<String> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        return predicate.test(input);
    }


}
