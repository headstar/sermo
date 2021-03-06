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

package com.headstartech.sermo.support;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.StateMachine;

/**
 * @author Per Johansson
 */
public interface StateMachineService<S, E extends DialogEvent> {

    StateMachine<S, E> acquireStateMachine(String machineId);

    void releaseStateMachine(String machineId, StateMachine<S, E> stateMachine);

    void releaseStateMachineOnException(String machineId, StateMachine<S, E> stateMachine);

}
