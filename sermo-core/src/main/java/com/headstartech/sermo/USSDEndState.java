package com.headstartech.sermo;

import org.springframework.statemachine.action.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class USSDEndState<S, E> {

    private final S id;
    private final List<Action<S, E>> entryActions;


    public USSDEndState(S id) { this(id, Collections.emptyList());}

    public USSDEndState(S id, Action<S, E> entryAction) {
        this(id,  Arrays.asList(entryAction));
    }

    public USSDEndState(S id, Collection<Action<S, E>> entryActions) {
        this.id = id;
        this.entryActions = new ArrayList<>(entryActions.stream().filter(e -> e != null).collect(Collectors.toCollection(ArrayList::new)));
    }

    public S getId() {
        return id;
    }

    public Collection<Action<S, E>> getEntryActions() {
        return entryActions;
    }

}
