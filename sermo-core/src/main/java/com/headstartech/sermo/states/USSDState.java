/*
 *  Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.headstartech.sermo.states;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.statemachine.actions.DefaultMenuScreenExitAction;
import org.springframework.statemachine.action.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class USSDState<S, E extends DialogEvent> {

    private final S id;
    private final List<Action<S, E>> entryActions;
    private final List<Action<S, E>> exitActions;


    public USSDState(S id, Action<S, E> entryAction) {
        this(id, entryAction, null);
    }

    public USSDState(S id, Action<S, E> entryAction, Action<S, E> exitAction) {
        this(id, Collections.singletonList(entryAction), Collections.singletonList(exitAction));
    }

    public USSDState(S id, Collection<Action<S, E>> entryActions, Collection<Action<S, E>> exitActions) {
        this.id = id;
        this.entryActions = new ArrayList<>(entryActions.stream().filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new)));
        this.exitActions = new ArrayList<>(exitActions.stream().filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new)));

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

    protected void addDefaultActions() {
        exitActions.add(new DefaultMenuScreenExitAction<>());
    }
}
