package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
class StateWrapperAction implements Action<String, Object> {

    public enum ActionEnum {ENTRY, EXIT, INTERNAL}

    private final USSDState delegate;
    private final ActionEnum actionEnum;

    public StateWrapperAction(USSDState delegate, ActionEnum actionEnum) {
        this.delegate = delegate;
        this.actionEnum = actionEnum;
    }

    @Override
    public void execute(StateContext<String, Object> context) {
        USSDSupport ussdSupport = context.getExtendedState().get(ExtendedStateKeys.SUPPORT_KEY, USSDSupport.class);
        String output = null;
        if (ActionEnum.ENTRY.equals(actionEnum)) {
            output = delegate.onEntry(ussdSupport);
        } else if (ActionEnum.EXIT.equals(actionEnum)) {
            delegate.onExit(ussdSupport, context.getEvent());
        } else if (ActionEnum.INTERNAL.equals(actionEnum)) {
            output = delegate.onInternal(ussdSupport, context.getEvent());
        } else {
            throw new IllegalStateException("unknown enum " + actionEnum.name());
        }
        if (output != null) {
            context.getExtendedState().getVariables().put(ExtendedStateKeys.OUTPUT_KEY, output);
        }
    }
}
