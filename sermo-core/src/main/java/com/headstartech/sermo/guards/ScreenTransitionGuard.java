package com.headstartech.sermo.guards;

import com.headstartech.sermo.ExtendedStateSupport;
import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.screen.InputMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class ScreenTransitionGuard<S, E extends MOInput> extends GuardBase<S, E> {

    private static final Logger log = LoggerFactory.getLogger(ScreenTransitionGuard.class);

    private final Object transitionId;

    public ScreenTransitionGuard(Object transitionId) {
        this.transitionId = transitionId;
    }

    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean res = false;
        InputMap inputMap = ExtendedStateSupport.getInputMap(context.getExtendedState());
        if(inputMap != null && inputMap.hasTransitionForInput(transitionId, input)) {
            res = true;
        }
        log.debug("Screen transition guard evaluation: result={}, input={}, transitionId={}", res, input, transitionId);
        return res;
    }
}
