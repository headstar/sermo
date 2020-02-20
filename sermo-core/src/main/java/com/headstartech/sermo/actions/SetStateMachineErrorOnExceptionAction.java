package com.headstartech.sermo.actions;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class SetStateMachineErrorOnExceptionAction<S, E> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {
        context.getStateMachine().setStateMachineError(context.getException());
    }
}
