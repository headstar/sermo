package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Optional;

import static com.headstartech.sermo.ExtendedStateKeys.INPUT_ITEM_DATA_KEY;

/**
 * @author Per Johansson
 */
public class MenuScreenExitAction<S, E> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {
        transferItemKey(context.getExtendedState(), context.getEvent());
        clearMenu(context.getExtendedState());
    }

    protected void clearMenu(ExtendedState extendedState) {
        extendedState.getVariables().remove(ExtendedStateKeys.INPUT_MAP_KEY);
    }

    protected void transferItemKey(ExtendedState extendedState, Object event) {
        @SuppressWarnings("unchecked")
        InputMap inputMap = (InputMap) extendedState.getVariables().get(ExtendedStateKeys.INPUT_MAP_KEY);
        if (inputMap != null) {
            if (event instanceof MOInput) {
                MOInput moInput = (MOInput) event;
                Optional<Object> itemData = inputMap.getItemDataForInput(moInput.getInput());
                if(itemData.isPresent()) {
                    extendedState.getVariables().put(INPUT_ITEM_DATA_KEY, itemData.get());
                }
            }
        }
    }
}
