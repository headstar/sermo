package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public abstract class MenuScreenEntryAction<S, E> implements Action<S, E> {

    protected void setScreenMenu(ExtendedState extendedState, Screen screen) {
        ExtendedStateSupport.setScreenMenuInputMap(extendedState, screen.getInputMap());
        ExtendedStateSupport.setOutput(extendedState, screen.getOutput());
    }
}
