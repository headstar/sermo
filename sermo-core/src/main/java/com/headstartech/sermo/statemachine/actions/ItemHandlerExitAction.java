package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.action.Action;

/**
 * Exit {@link Action} assuming the object associated with a {@link MenuItem} is an instance {@link OnItemHandler}.
 * The action calls {@link OnItemHandler#handle(ExtendedState)} 
 * 
 * @param <S>
 * @param <E>
 */
public class ItemHandlerExitAction<S, E extends DialogEvent> extends AbstractItemObjectExitAction<S, E> {

    @Override
    protected void handleItemObject(ExtendedState extendedState, Object itemObject) {
        if(!(itemObject instanceof OnItemHandler)) {
            throw new IllegalStateException(String.format("Expected OnItemHandler, not %s", itemObject.getClass().getName()));
        }
        ((OnItemHandler) itemObject).handle(extendedState);
    }
}
