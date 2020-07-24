package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.ExtendedState;

public class ItemHandlerExitAction<S, E extends DialogEvent> extends AbstractItemObjectExitAction<S, E> {

    @Override
    protected void handleItemObject(ExtendedState extendedState, Object itemObject) {
        if(!(itemObject instanceof OnItemHandler)) {
            throw new IllegalStateException(String.format("Expected OnItemHandler, not %s", itemObject.getClass().getName()));
        }
        ((OnItemHandler) itemObject).handle(extendedState);
    }
}
