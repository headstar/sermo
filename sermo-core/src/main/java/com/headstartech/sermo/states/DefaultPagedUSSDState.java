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

/**
 * Default implementation of {@link PagedUSSDState}.
 *
 * @author Per Johansson
 */
public class DefaultPagedUSSDState<S, E extends DialogEvent>  implements PagedUSSDState<S, E> {

    private final USSDState<S, E> ussdState;
    private final Action<S, E> internalPageAction;

    public DefaultPagedUSSDState(USSDState<S, E> ussdState, Action<S, E> internalPageAction) {
        this.ussdState = ussdState;
        this.internalPageAction = internalPageAction;
    }

    @Override
    public S getId() {
        return ussdState.getId();
    }

    @Override
    public Collection<Action<S, E>> getEntryActions() {
        return ussdState.getEntryActions();
    }

    @Override
    public Collection<Action<S, E>> getExitActions() {
        return ussdState.getExitActions();
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public Action<S, E> toNextOrToPreviousPageAction() {
        return internalPageAction;
    }
}
