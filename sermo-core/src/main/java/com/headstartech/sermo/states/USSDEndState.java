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
import org.springframework.statemachine.action.Action;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Per Johansson
 */
public class USSDEndState<S, E extends DialogEvent> extends DefaultUSSDState<S, E> {

    public USSDEndState(S id) { this(id, Collections.emptyList());}

    public USSDEndState(S id, Action<S, E> entryAction) {
        this(id, Collections.singletonList(entryAction));
    }

    public USSDEndState(S id, Collection<Action<S, E>> entryActions) {
        super(id, entryActions, Collections.emptyList());
    }

}
