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

import com.headstartech.sermo.MOInput;
import org.springframework.statemachine.action.Action;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class USSDEndState<S, E> extends MOInput {

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
