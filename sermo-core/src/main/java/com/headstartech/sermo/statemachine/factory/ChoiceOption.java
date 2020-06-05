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

package com.headstartech.sermo.statemachine.factory;

import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.guard.Guard;

/**
 * @author Per Johansson
 */
public class ChoiceOption<S, E> {

    private final S target;
    private final Guard<S, E> guard;
    private final Action<S, E> action;

    public ChoiceOption(S target, Guard<S, E> guard) {
        this(target, guard, null);
    }

    public ChoiceOption(S target, Guard<S, E> guard, Action<S, E> action) {
        this.target = target;
        this.guard = guard;
        this.action = action;
    }

    public S getTarget() {
        return target;
    }

    public Guard<S, E> getGuard() {
        return guard;
    }

    public Action<S, E> getAction() {
        return action;
    }
}
