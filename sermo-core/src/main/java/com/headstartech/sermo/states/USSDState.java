package com.headstartech.sermo.states;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.action.Action;

import java.util.Collection;

/**
 * Core interface representing a USSD state.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface USSDState<S, E extends DialogEvent> {

    /**
     * Gets id of state.
     *
     * @return the state id
     */
    S getId();

    /**
     * Gets actions executed when the state is entered.
     *
     * @return the entry actions
     */
    Collection<Action<S, E>> getEntryActions();

    /**
     * Gets actions executed when the state is exited.
     *
     * @return the exit actions
     */
    Collection<Action<S, E>> getExitActions();

    /**
     * Indicates whether the state is an end state or not.
     *
     * @return {@code true} if it's an end state, {@code false} otherwise
     */
    boolean isEnd();
}
