package com.headstartech.sermo;

import java.util.Optional;

import static com.headstartech.sermo.ExtendedStateKeys.INPUT_ITEM_KEY;

/**
 * @author Per Johansson
 */
public abstract class MenuUSSDState extends USSDStateAdapter {

    @Override
    public void onExit(USSDSupport ussdSupport, Object event) {
        transferItemKey(ussdSupport, event);
        clearMenu(ussdSupport);
    }

    protected void setMenu(USSDSupport ussdSupport, Screen screen) {
        ussdSupport.getVariables().put(ExtendedStateKeys.INPUT_MAP_KEY, screen.getInputMap());
    }

    protected void clearMenu(USSDSupport ussdSupport) {
        ussdSupport.getVariables().remove(ExtendedStateKeys.INPUT_MAP_KEY);
    }

    private void transferItemKey(USSDSupport ussdSupport, Object event) {
        @SuppressWarnings("unchecked")
        InputMap inputMap = (InputMap) ussdSupport.getVariables().get(ExtendedStateKeys.INPUT_MAP_KEY);
        if (inputMap != null) {
            if (event instanceof MOInput) {
                MOInput moInput = (MOInput) event;
                Optional<Object> itemData = inputMap.getItemDataForInput(moInput.getInput());
                if(itemData.isPresent()) {
                    ussdSupport.getVariables().put(INPUT_ITEM_KEY, itemData.get());
                }
            }
        }
    }
}
