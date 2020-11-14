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

package com.headstartech.sermo;

import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.StateMachineDeleter;
import com.headstartech.sermo.support.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public interface SermoDialogExecutor<S, E extends DialogEvent> {

    DialogEventResult applyEvent(String sessionId, E event) throws SermoDialogException;

    void register(SermoDialogListener<E> listener);

    void unregister(SermoDialogListener<E> listener);

}