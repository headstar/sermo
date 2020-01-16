package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public abstract class MenuUSSDState implements USSDState {

    @Override
    public void onExit(USSDSupport ussdSupport) {
        clearMenu(ussdSupport);
    }

    protected void setMenu(USSDSupport ussdSupport, Menu menu) {
        ussdSupport.getVariables().put(ExtendedStateKeys.INPUT_TRANSITION_MAP, menu.getInputTransitionKeyMap());
        ussdSupport.getVariables().put(ExtendedStateKeys.INPUT_ITEM_MAP, menu.getInputItemKeyMap());
    }

    protected void clearMenu(USSDSupport ussdSupport) {
        ussdSupport.getVariables().remove(ExtendedStateKeys.INPUT_TRANSITION_MAP);
    }
}
