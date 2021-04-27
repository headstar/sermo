package com.headstartech.sermo.states;


import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.action.Action;

/**
 * Interface for states supporting paging.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface PagedUSSDState<S, E extends DialogEvent> extends USSDState<S, E> {

    Action<S, E> toNextOrToPreviousPageAction();
}
