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

package com.headstartech.sermo.statemachine;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

/**
 * Default implementation of a {@link StateMachinePool} returning a new instance every time.
 *
 *
 * @author Per Johansson
 */
public class DefaultStateMachinePool<S, E> implements StateMachinePool<S,E> {

    private final StateMachineFactory<S, E> stateMachineFactory;

    public DefaultStateMachinePool(StateMachineFactory<S, E> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public StateMachine<S, E> borrowStateMachine() {
        return stateMachineFactory.getStateMachine();
    }

    @Override
    public void returnStateMachine(StateMachine<S, E> stateMachine) {
        // do nothing
    }
}
