package com.headstartech.sermo.guards;


import com.headstartech.sermo.MOInput;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

/**
 * @author Per Johansson
 */
public abstract class GuardBase<S, E extends MOInput> implements Guard<S, E> {

    @Override
    public boolean evaluate(StateContext<S, E> context) {
        if(context.getEvent() != null && context.getEvent().getInput() != null) {
            return doEvaluate(context,  context.getEvent().getInput());
        }
        return false;
    }

    protected abstract boolean doEvaluate(StateContext<S, E> context, String input);
}
