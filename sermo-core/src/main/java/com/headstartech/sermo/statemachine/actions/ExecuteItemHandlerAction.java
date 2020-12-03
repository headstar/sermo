package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.support.DefaultDialogExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.action.Action;

/**
 * Exit {@link Action} assuming the object associated with a {@link com.headstartech.sermo.screen.MenuItem} is an instance of {@link OnItemHandler}.
 *
 * The action calls {@link OnItemHandler#handle(ExtendedState)} 
 * 
 * @param <S>
 * @param <E>
 */
public class ExecuteItemHandlerAction<S, E extends DialogEvent> extends AbstractScreenCleanupAction<S, E> {

    private static final Logger log = LoggerFactory.getLogger(ExecuteItemHandlerAction.class);

    @Override
    protected void handleItemObject(ExtendedState extendedState, Object onItemHandler) {
        if(!(onItemHandler instanceof OnItemHandler)) {
            throw new IllegalStateException(String.format("Expected OnItemHandler, not %s", onItemHandler.getClass().getName()));
        }
        log.debug("Executing OnItemHandler: onItemHandler={}", onItemHandler);
        ((OnItemHandler) onItemHandler).handle(extendedState);
    }
}
