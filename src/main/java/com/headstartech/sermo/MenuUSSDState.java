package com.headstartech.sermo;

import java.util.Map;

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

    protected void setMenu(USSDSupport ussdSupport, Menu menu) {
        ussdSupport.getVariables().put(ExtendedStateKeys.INPUT_TRANSITION_MAP, menu.getInputTransitionKeyMap());
        ussdSupport.getVariables().put(ExtendedStateKeys.INPUT_ITEM_MAP, menu.getInputItemKeyMap());
    }

    protected void clearMenu(USSDSupport ussdSupport) {
        ussdSupport.getVariables().remove(ExtendedStateKeys.INPUT_TRANSITION_MAP);
        ussdSupport.getVariables().remove(ExtendedStateKeys.INPUT_ITEM_MAP);
    }

    private void transferItemKey(USSDSupport ussdSupport, Object event) {
        @SuppressWarnings("unchecked")
        Map<String, Object> inputItemMap = (Map<String, Object>) ussdSupport.getVariables().get(ExtendedStateKeys.INPUT_ITEM_MAP);
        if (inputItemMap != null) {
            if (event instanceof MOInput) {
                MOInput moInput = (MOInput) event;
                Object inputItemKey = inputItemMap.get(moInput.getInput());
                if (inputItemKey != null) {
                    ussdSupport.getVariables().put(INPUT_ITEM_KEY, inputItemKey);
                }
            }
        }
    }
}
