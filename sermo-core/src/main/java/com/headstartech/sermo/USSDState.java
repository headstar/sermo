package com.headstartech.sermo;

import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class USSDState<S, E> {

    private final S id;
    private final Action<S, E> entryAction;
    private final Action<S, E> exitAction;

    public USSDState(S id, Action<S, E> entryAction, Action<S, E> exitAction) {
        this.id = id;
        this.entryAction = entryAction;
        this.exitAction = exitAction;
    }

    public USSDState(S id, Action<S, E> entryAction) {
        this(id, entryAction, null);
    }

    public S getId() {
        return id;
    }

    public Action<S, E> getEntryAction() {
        return entryAction;
    }

    public Action<S, E> getExitAction() {
        return exitAction;
    }
}
