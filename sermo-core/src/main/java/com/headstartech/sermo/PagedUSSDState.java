package com.headstartech.sermo;

import org.springframework.statemachine.action.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
