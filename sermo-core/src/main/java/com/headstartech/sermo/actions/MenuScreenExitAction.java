package com.headstartech.sermo.actions;

import com.headstartech.sermo.USSDSystemConstants;
import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.screen.InputMap;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Optional;

import static com.headstartech.sermo.USSDSystemConstants.INPUT_ITEM_DATA_KEY;

/**
 * @author Per Johansson
 */
public class MenuScreenExitAction<S, E extends MOInput> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {
        transferItemKey(context.getExtendedState(), context.getEvent());
        clearMenu(context.getExtendedState());
    }

    protected void clearMenu(ExtendedState extendedState) {
        extendedState.getVariables().remove(USSDSystemConstants.INPUT_MAP_KEY);
    }

    protected void transferItemKey(ExtendedState extendedState, MOInput event) {
        @SuppressWarnings("unchecked")
        InputMap inputMap = (InputMap) extendedState.getVariables().get(USSDSystemConstants.INPUT_MAP_KEY);
        if (inputMap != null) {
            Optional<Object> itemData = inputMap.getItemDataForInput(event.getInput());
            if(itemData.isPresent()) {
                extendedState.getVariables().put(INPUT_ITEM_DATA_KEY, itemData.get());
            }
        }
    }
}
