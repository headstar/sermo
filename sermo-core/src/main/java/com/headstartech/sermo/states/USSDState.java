package com.headstartech.sermo.states;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.action.Action;

import java.util.Collection;

/**
 * Core interface representing a USSD state.
 *
 * @param <S>
 * @param <E>
 */
public interface USSDState<S, E extends DialogEvent> {

    /**
     * Gets id of state.
     *
     * @return
     */
    S getId();

    /**
     * Gets actions executed when the state is entered.
     *
     * @return
     */
    Collection<Action<S, E>> getEntryActions();

    /**
     * Gets actions executed when the state is exited.
     *
     * @return
     */
    Collection<Action<S, E>> getExitActions();

    /**
     * Indicates whether the state is an end state or not.
     *
     * @return
     */
    boolean isEnd();
}
