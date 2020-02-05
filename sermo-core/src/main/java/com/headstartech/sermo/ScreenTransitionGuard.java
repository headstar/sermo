package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

/**
 * @author Per Johansson
 */
public class ScreenTransitionGuard implements Guard<String, Object> {

    private final Object transitionName;

    public ScreenTransitionGuard(Object transitionName) {
        this.transitionName = transitionName;
    }

    @Override
    public boolean evaluate(StateContext<String, Object> context) {
        boolean res = false;
        Object event = context.getEvent();
        if(event instanceof MOInput) {
            MOInput moInput = (MOInput) event;
            InputMap input = ExtendedStateKeys.getInputMap(context.getExtendedState());
            if(input != null && input.hasTransitionForInput(transitionName, moInput.getInput())) {
                res = true;
            }
        }
        return res;
    }
}
