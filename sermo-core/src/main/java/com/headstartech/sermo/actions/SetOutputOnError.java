package com.headstartech.sermo.actions;

import com.headstartech.sermo.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public abstract class SetOutputOnError<S,E> implements Action<S, E> {

    @Override
    public final void execute(StateContext<S, E> context) {
        ExtendedStateSupport.setOutput(context.getExtendedState(), getOutput(context));
    }

    protected abstract String getOutput(StateContext<S, E> context);
}
