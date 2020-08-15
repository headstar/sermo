package com.headstartech.sermo.states;


import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.action.Action;

/**
 * Interface for states supporting paging.
 *
 * @param <S>
 * @param <E>
 */
public interface PagedUSSDState<S, E extends DialogEvent> extends USSDState<S, E> {

    Action<S, E> toNextOrToPreviousPageAction();
}
