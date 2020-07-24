package com.headstartech.sermo.states;


import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.action.Action;

/**
 *
 * TODO: rename to PagedUSSDState when PagedUSSDState class has been renamed to DefaultPagedUSSDState
 *
 * @param <S>
 * @param <E>
 */
public interface IPagedUSSDState<S, E extends DialogEvent> {

    Action<S, E> toNextOrToPreviousPageAction();
}
