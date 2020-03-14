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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Per Johansson
 */
public class ConcurrentHashMapPersist<S, E> implements StateMachinePersist<S, E, String>, StateMachineDeleter<String> {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentHashMapPersist.class);

    private final ConcurrentHashMap<String, StateMachineContext<S, E>> contexts = new ConcurrentHashMap<>();

    @Override
    public void write(StateMachineContext<S, E> context, String contextObj) {
        log.debug("Writing state machine context: contextObj={}", contextObj);
        contexts.put(contextObj, context);
    }

    @Override
    public StateMachineContext<S, E> read(String contextObj) {
        log.debug("Reading state machine context: contextObj={}", contextObj);
        return contexts.get(contextObj);
    }

    @Override
    public void delete(String contextObj) {
        log.debug("Deleting state machine context: contextObj={}", contextObj);
        contexts.remove(contextObj);
    }
}
