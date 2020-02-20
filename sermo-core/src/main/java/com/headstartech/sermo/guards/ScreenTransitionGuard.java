package com.headstartech.sermo.guards;

import com.headstartech.sermo.ExtendedStateKeys;
import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.screen.InputMap;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class ScreenTransitionGuard<S, E extends MOInput> extends GuardBase<S, E> {

    private final Object transitionName;

    public ScreenTransitionGuard(Object transitionName) {
        this.transitionName = transitionName;
    }

    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean guard = false;
        if(input != null) {
            InputMap inputMap = ExtendedStateKeys.getInputMap(context.getExtendedState());
            if(inputMap != null && inputMap.hasTransitionForInput(transitionName, input)) {
                guard = true;
            }
        }
        return guard;
    }
}
