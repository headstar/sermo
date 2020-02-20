package com.headstartech.sermo.guards;

import com.headstartech.sermo.MOInput;
import org.springframework.statemachine.StateContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Per Johansson
 */
public class InitialTransitionGuard<S, E extends MOInput> extends GuardBase<S, E> {

    private final Pattern pattern;

    public InitialTransitionGuard(Pattern pattern) {
        this.pattern = pattern;
    }

    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean res = false;
        if(input != null) {
            Matcher m = pattern.matcher(input);
            res = m.matches();
        }
        return res;
    }

}
