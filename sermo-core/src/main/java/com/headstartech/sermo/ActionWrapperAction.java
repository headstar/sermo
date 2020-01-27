package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Optional;

/**
 * @author Per Johansson
 */
class ActionWrapperAction implements Action<String, Object> {

    private final USSDAction ussdAction;

    public ActionWrapperAction(USSDAction ussdAction) {
        this.ussdAction = ussdAction;
    }

    @Override
    public void execute(StateContext<String, Object> context) {
        USSDSupport ussdSupport = context.getExtendedState().get(ExtendedStateKeys.SUPPORT_KEY, USSDSupport.class);
        ussdAction.execute(ussdSupport, Optional.ofNullable(context.getEvent()));
    }
}
