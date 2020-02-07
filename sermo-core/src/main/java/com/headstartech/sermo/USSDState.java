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
public class USSDState<S, E> {

    private final S id;
    private final List<Action<S, E>> entryActions;
    private final List<Action<S, E>> exitActions;


    public USSDState(S id, Action<S, E> entryAction) {
        this(id, entryAction, null);
    }

    public USSDState(S id, Action<S, E> entryAction, Action<S, E> exitAction) {
        this(id, Arrays.asList(entryAction), Arrays.asList(exitAction));
    }

    public USSDState(S id, Collection<Action<S, E>> entryActions, Collection<Action<S, E>> exitActions) {
        this.id = id;
        this.entryActions = new ArrayList<>(entryActions.stream().filter(e -> e != null).collect(Collectors.toCollection(ArrayList::new)));
        this.exitActions = new ArrayList<>(exitActions.stream().filter(e -> e != null).collect(Collectors.toCollection(ArrayList::new)));

        addDefaultActions();
    }

    public S getId() {
        return id;
    }

    public Collection<Action<S, E>> getEntryActions() {
        return entryActions;
    }

    public Collection<Action<S, E>> getExitActions() {
        return exitActions;
    }

    private void addDefaultActions() {
        exitActions.add(new MenuScreenExitAction<>());
    }
}
