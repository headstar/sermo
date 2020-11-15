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

package com.headstartech.sermo.persist;

import com.headstartech.sermo.statemachine.StateMachineDeleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Implementation of {@link StateMachinePersist} and {@link StateMachineDeleter} using a {@link Cache}.
 *
 * Use with e.g. a {@link ConcurrentMapCache}.
 *
 * @author Per Johansson
 */
public class CachePersist<S, E> implements StateMachinePersist<S, E, String>, StateMachineDeleter<String> {

    private static final Logger log = LoggerFactory.getLogger(CachePersist.class);

    private final StateMachineContext<S, E> EMPTY_CONTEXT = new DefaultStateMachineContext<>(new ArrayList<>(), null, null, null, new DefaultExtendedState(), new HashMap<>(), null);

    private final Cache cache;

    public CachePersist(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void write(StateMachineContext<S, E> context, String contextObj) {
        log.trace("Writing state machine context: contextObj={}, context={}", contextObj, context);
        cache.put(contextObj, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public StateMachineContext<S, E> read(String contextObj) {
        log.debug("Reading state machine context: contextObj={}", contextObj);
        StateMachineContext<S, E> context = (StateMachineContext<S, E>) cache.get(contextObj, StateMachineContext.class);
        if(context == null) {
            // Workaround for a possible Spring Statemachine bug.
            // Calling org.springframework.statemachine.persist.DefaultStateMachinePersister.restore with a context == null
            // resulting in a state machine already in the initial state. If the intial state has an associated action,
            // it won't be executed.
            log.debug("No state machine context found, returning empty context: contextObj={}", contextObj);
            return EMPTY_CONTEXT;
        } else {
            log.trace("State machine context read: contextObj={}, context={}", contextObj, context);
            return context;
        }
    }

    @Override
    public void delete(String contextObj) {
        log.debug("Deleting state machine context: contextObj={}", contextObj);
        cache.evictIfPresent(contextObj);
    }
}
