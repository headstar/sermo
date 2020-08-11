package com.headstartech.sermo.states;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.action.Action;

import java.util.Collection;

public interface USSDState<S, E extends DialogEvent> {

    S getId();

    Collection<Action<S, E>> getEntryActions();

    Collection<Action<S, E>> getExitActions();

    boolean isEnd();
}
