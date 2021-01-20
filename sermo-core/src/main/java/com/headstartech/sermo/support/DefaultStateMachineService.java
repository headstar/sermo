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
import com.headstartech.sermo.DialogPersisterException;
import com.headstartech.sermo.statemachine.DefaultStateMachinePool;
import com.headstartech.sermo.statemachine.StateMachineDeleter;
import com.headstartech.sermo.statemachine.StateMachinePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Per Johansson
 */
public class DefaultStateMachineService<S, E extends DialogEvent> implements StateMachineService<S, E> {

    private static final Logger log = LoggerFactory.getLogger(DefaultStateMachineService.class);

    private final StateMachinePool<S, E> stateMachinePool;
    private final StateMachinePersister<S, E, String> stateMachinePersister;
    private StateMachineDeleter<String> stateMachineDeleter;


    public DefaultStateMachineService(StateMachineFactory<S, E> stateMachineFactory, StateMachinePersist<S, E, String> stateMachinePersist, StateMachineDeleter<String> stateMachineDeleter) {
        this(new DefaultStateMachinePool<>(stateMachineFactory),
                stateMachinePersist,
                stateMachineDeleter);
    }

    public DefaultStateMachineService(StateMachinePool<S, E> stateMachinePool, StateMachinePersist<S, E, String> stateMachinePersist, StateMachineDeleter<String> stateMachineDeleter) {
        this.stateMachinePool = stateMachinePool;
        this.stateMachinePersister = new DefaultStateMachinePersister<>(new StateMachinePersistWrapper<>(stateMachinePersist));
        this.stateMachineDeleter = stateMachineDeleter;
    }

    @Override
    public StateMachine<S, E> acquireStateMachine(String machineId) {
        log.debug("Acquiring state machine");
        StateMachine<S, E> stateMachine = stateMachinePool.borrowStateMachine();
        try {
            log.debug("Restoring state machine machine state: machineId={}", machineId);
            stateMachinePersister.restore(stateMachine, machineId);
        } catch (Exception e) {
            throw new DialogPersisterException(String.format("Unable to restore state machine: %s", machineId), e);
        }

        if(stateMachineIsCompleteOrHasError(stateMachine)) {
            // might happen if StateMachineDeleter is asynchronous
            log.debug("Restored state machine completed or in error, resetting state machine: machineId={}", machineId);
            resetStateMachine(stateMachine);
        }

        return stateMachine;
    }

    @Override
    public void releaseStateMachine(String machineId, StateMachine<S, E> stateMachine) {
        boolean exceptionOnPersist = false;
        try {
            stateMachinePersister.persist(stateMachine, machineId);
        } catch (Exception e) {
            exceptionOnPersist = true;
            throw new DialogPersisterException(String.format("Unable to persist context to store: %s", machineId), e);
        } finally {
            if(exceptionOnPersist || stateMachineIsCompleteOrHasError(stateMachine)) {
                try {
                    stateMachineDeleter.delete(machineId);
                } catch(RuntimeException e) {
                    log.warn(String.format("Error when deleting state machine: %s", machineId), e);
                }
            }
            stateMachinePool.returnStateMachine(stateMachine);
        }
    }

    @Override
    public void releaseStateMachineOnException(String machineId, StateMachine<S, E> stateMachine) {
        try {
            stateMachineDeleter.delete(machineId);
        } catch(RuntimeException e) {
            log.warn(String.format("Error when deleting state machine: %s", machineId), e);
        }
        stateMachinePool.returnStateMachine(stateMachine);
    }

    protected boolean stateMachineIsCompleteOrHasError(StateMachine<S, E> stateMachine) {
        return stateMachine.isComplete() || stateMachine.hasStateMachineError();
    }

    protected void resetStateMachine(StateMachine<S,E> stateMachine) {
        stateMachine.stop();
        stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.resetStateMachine(null));
        stateMachine.start();
    }

    /**
     * Wrapper to handle the case where the read {@link StateMachineContext} is {@code null}.
     *
     * @param <S>
     * @param <E>
     */
    private static class StateMachinePersistWrapper<S, E> implements StateMachinePersist<S, E, String> {

        private final StateMachineContext<S, E> EMPTY_CONTEXT = new DefaultStateMachineContext<>(new ArrayList<>(), null, null, null, new DefaultExtendedState(), new HashMap<>(), null);

        private StateMachinePersist<S, E, String> delegate;

        public StateMachinePersistWrapper(StateMachinePersist<S, E, String> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void write(StateMachineContext<S, E> context, String contextObj) throws Exception {
            delegate.write(context, contextObj);
        }

        @Override
        public StateMachineContext<S, E> read(String contextObj) throws Exception {
            StateMachineContext<S, E> context = delegate.read(contextObj);
            if(context == null) {
                // Workaround for a possible Spring Statemachine bug.
                // Calling org.springframework.statemachine.persist.DefaultStateMachinePersister.restore with a context == null
                // resulting in a state machine already in the initial state. If the intial state has an associated action,
                // it won't be executed.
                log.debug("No state machine context found, returning empty context: contextObj={}", contextObj);
                return EMPTY_CONTEXT;
            } else {
                return context;
            }
        }
    }
}