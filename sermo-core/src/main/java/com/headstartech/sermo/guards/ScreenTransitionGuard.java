package com.headstartech.sermo.guards;

import com.headstartech.sermo.ExtendedStateKeys;
import com.headstartech.sermo.screen.InputMap;
import org.springframework.statemachine.StateContext;

import java.util.function.Function;

/**
 * @author Per Johansson
 */
public class ScreenTransitionGuard<S, E> extends InputGuardBase<S, E> {

    private final Object transitionName;

    public ScreenTransitionGuard(Function<E, String> eventToInput, Object transitionName) {
        super(eventToInput);
        this.transitionName = transitionName;
    }

    @Override
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
