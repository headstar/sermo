package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public abstract class MenuScreenEntryAction<S, E> implements Action<S, E> {


    // TODO: remove??
    protected void setScreenMenu(ExtendedState extendedState, Screen screen) {
        extendedState.getVariables().put(ExtendedStateKeys.INPUT_MAP_KEY, screen.getInputMap());
    }


}
