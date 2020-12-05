package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.SystemConstants;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public abstract class AbstractPagedScreenInternalAction<S, E extends DialogEvent> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {

        Object transitionId = ExtendedStateSupport.getTransition(context.getExtendedState(), (context.getEvent()).getInput())
                .orElseThrow(() -> new IllegalStateException("AbstractPagedInternalAction executed with no transition id set"));
        if(SystemConstants.NEXT_PAGE_KEY.equals(transitionId)) {
            handleNextPage(context);
        } else if(SystemConstants.PREVIOUS_PAGE_KEY.equals(transitionId)) {
            handlePreviousPage(context);
        } else {
            throw new IllegalStateException(String.format("AbstractPagedInternalAction executed with unknown transition id: transitionId=%s", transitionId));
        }
    }

    protected abstract void handleNextPage(StateContext<S,E> context);

    protected abstract void handlePreviousPage(StateContext<S,E> context);

}

