package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

/**
 * @author Per Johansson
 */
public class MenuItemGuard implements Guard<String, Object> {

    private final Object transitionName;

    public MenuItemGuard(Object transitionName) {
        this.transitionName = transitionName;
    }

    @Override
    public boolean evaluate(StateContext<String, Object> context) {
        boolean res = false;
        Object event = context.getEvent();
        if(event instanceof MOInput) {
            MOInput moInput = (MOInput) event;
            InputMap input = ExtendedStateKeys.getInputMap(context.getExtendedState());
            if(input != null && input.hasTransitionNameForInput(transitionName, moInput.getInput())) {
                context.getExtendedState().getVariables().put(ExtendedStateKeys.TRANSITION_KEY, transitionName);
                res = true;
            }
        }
        return res;    }
}
