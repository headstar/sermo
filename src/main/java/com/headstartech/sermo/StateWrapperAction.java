package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Map;

import static com.headstartech.sermo.ExtendedStateKeys.INPUT_ITEM_KEY;

/**
 * @author Per Johansson
 */
class StateWrapperAction implements Action<String, Object> {

    public enum ActionEnum {ENTRY, EXIT, EVENT}

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
            delegate.onExit(ussdSupport);
            transferItemKey(context);
        } else if (ActionEnum.EVENT.equals(actionEnum)) {
            output = delegate.onEvent(ussdSupport, context.getEvent());
        } else {
            throw new IllegalStateException("unknown enum " + actionEnum.name());
        }
        if (output != null) {
            context.getExtendedState().getVariables().put(ExtendedStateKeys.OUTPUT_KEY, output);
        }
    }

    private void transferItemKey(StateContext<String, Object> context) {
        Map<Object, Object> inputItemMap = ExtendedStateKeys.getInputTransitionKeyMap(context.getExtendedState());
        if (inputItemMap != null) {
            Object event = context.getEvent();
            if (event instanceof MOInput) {
                MOInput moInput = (MOInput) event;
                Object inputItemKey = inputItemMap.get(moInput.getInput());
                if (inputItemKey != null) {
                    context.getExtendedState().getVariables().put(INPUT_ITEM_KEY, inputItemKey);
                }
            }
        }
    }
}
