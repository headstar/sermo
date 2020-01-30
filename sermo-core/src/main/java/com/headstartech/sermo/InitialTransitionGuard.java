package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Per Johansson
 */
public class InitialTransitionGuard implements Guard<String, Object> {

    private final Pattern pattern;

    public InitialTransitionGuard(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean evaluate(StateContext<String, Object> context) {
        boolean res = false;
        Object event = context.getEvent();
        if(event instanceof MOInput) {
            MOInput moInput = (MOInput) event;
            Matcher m = pattern.matcher(moInput.getInput());
            res = m.matches();
        }
        return res;
    }
}
