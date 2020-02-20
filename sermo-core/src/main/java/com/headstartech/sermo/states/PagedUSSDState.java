package com.headstartech.sermo.states;

import com.headstartech.sermo.actions.PagedMenuScreenInternalAction;
import org.springframework.statemachine.action.Action;

import java.util.Collection;

/**
 * @author Per Johansson
 */
public class PagedUSSDState<S, E> extends USSDState<S, E> {

    public PagedUSSDState(S id, Action<S, E> entryAction) {
        super(id, entryAction);
    }

    public PagedUSSDState(S id, Action<S, E> entryAction, Action<S, E> exitAction) {
        super(id, entryAction, exitAction);
    }

    public PagedUSSDState(S id, Collection<Action<S, E>> entryActions, Collection<Action<S, E>> exitActions) {
        super(id, entryActions, exitActions);
    }

    public Action<S, E> toNextOrToPreviousPageAction() {
        return new PagedMenuScreenInternalAction<>();
    }
}
