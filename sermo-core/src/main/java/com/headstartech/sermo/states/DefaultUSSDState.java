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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class DefaultUSSDState<S, E extends DialogEvent> implements USSDState<S, E> {

    private final S id;
    private final List<Action<S, E>> entryActions;
    private final List<Action<S, E>> exitActions;
    private final boolean end;

    public DefaultUSSDState(S id, List<Action<S, E>> entryActions, List<Action<S, E>> exitActions, boolean end) {
        this.id = id;
        this.entryActions = entryActions;
        this.exitActions = exitActions;
        this.end = end;
    }

    public DefaultUSSDState(S id, Action<S, E> entryAction, boolean end) {
        this(id, entryAction, null, end);
    }

    public DefaultUSSDState(S id, Action<S, E> entryAction, Action<S, E> exitAction, boolean end) {
        this(id, Collections.singletonList(entryAction), Collections.singletonList(exitAction), end);
    }

    public DefaultUSSDState(S id, Collection<Action<S, E>> entryActions, Collection<Action<S, E>> exitActions, boolean end) {
        this.id = id;
        this.entryActions = entryActions.stream().filter(Objects::nonNull).collect(Collectors.toList());
        this.exitActions = exitActions.stream().filter(Objects::nonNull).collect(Collectors.toList());
        this.end = end;
    }

    @Override
    public S getId() {
        return id;
    }

    @Override
    public Collection<Action<S, E>> getEntryActions() {
        return entryActions;
    }

    @Override
    public Collection<Action<S, E>> getExitActions() {
        return exitActions;
    }

    @Override
    public boolean isEnd() {
        return end;
    }

    public static <S, E extends DialogEvent> Builder<S, E> builder(S id) {
        return new Builder<>(id);
    }

    public static class Builder<S, E extends DialogEvent> {
        private final S id;
        private final List<Action<S, E>> entryActions;
        private final List<Action<S, E>> exitActions;
        private boolean end = false;

        public Builder(S id) {
            this.id = id;
            this.entryActions = new ArrayList<>();
            this.exitActions = new ArrayList<>();
        }

        public Builder<S, E> withEntryAction(Action<S, E> action) {
            entryActions.add(action);
            return this;
        }

        public Builder<S, E> withExitAction(Action<S, E> action) {
            exitActions.add(action);
            return this;
        }

        public Builder<S, E> withEnd(boolean end) {
            this.end = end;
            return this;
        }

        public DefaultUSSDState<S, E> build() {
            return new DefaultUSSDState<>(id, entryActions, exitActions, end);
        }
    }

}
