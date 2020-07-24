package com.headstartech.sermo.states;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.statemachine.actions.ItemHandlerExitAction;
import org.springframework.statemachine.action.Action;

public class DefaultInputUSSDState<S, E extends DialogEvent> extends USSDState<S, E> {

    public DefaultInputUSSDState(S id, Action<S, E> entryAction) {
        super(id, entryAction, new ItemHandlerExitAction<>());
    }


    // TODO: remove method in super class
    @Override
    protected void addDefaultActions() {
        // do nothing
    }

}
