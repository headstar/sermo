package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Per Johansson
 */
public class InitialTransitionGuard<S, E> extends InputGuardBase<S, E> {

    private final Pattern pattern;

    public InitialTransitionGuard(Function<E, String> eventToInput, Pattern pattern) {
        super(eventToInput);
        this.pattern = pattern;
    }

    @Override
    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean res = false;
        if(input != null) {
            Matcher m = pattern.matcher(input);
            res = m.matches();
        }
        return res;
    }

}
