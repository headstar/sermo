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
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

/**
 * @author Per Johansson
 */
public class DefaultSermoStateMachineService<S, E extends MOInput> implements SermoStateMachineService<S, E> {

    private static final Logger log = LoggerFactory.getLogger(DefaultSermoStateMachineService.class);

    private final StateMachinePool<S, E> stateMachinePool;
    private final StateMachinePersister<S, E, String> stateMachinePersister;
    private StateMachineDeleter<String> stateMachineDeleter;


    public DefaultSermoStateMachineService(StateMachineFactory<S, E> stateMachineFactory, StateMachinePersist<S, E, String> stateMachinePersist, StateMachineDeleter<String> stateMachineDeleter) {
        this(new DefaultStateMachinePool<>(stateMachineFactory), new DefaultStateMachinePersister<>(stateMachinePersist), stateMachineDeleter);
    }

    public DefaultSermoStateMachineService(StateMachinePool<S, E> stateMachinePool, StateMachinePersister<S, E, String> stateMachinePersister, StateMachineDeleter<String> stateMachineDeleter) {
        this.stateMachinePool = stateMachinePool;
        this.stateMachinePersister = stateMachinePersister;
        this.stateMachineDeleter = stateMachineDeleter;
    }

    @Override
    public StateMachine<S, E> acquireStateMachine(String machineId) {
        log.debug("Acquiring state machine");
        StateMachine<S, E> stateMachine = stateMachinePool.getStateMachine();
        try {
            log.debug("Restoring state machine machine state: machineId={}", machineId);
            stateMachinePersister.restore(stateMachine, machineId);
        } catch (Exception e) {
            throw new SermoException(String.format("Unable to restore state machine: %s", machineId), e);
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
            throw new SermoException(String.format("Unable to persist context to store: %s", machineId), e);
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


}