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

import com.headstartech.sermo.*;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.StateMachineDeleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class DefaultSermoDialogExecutor<S, E extends DialogEvent> implements SermoDialogExecutor<S, E> {

    private static final Logger log = LoggerFactory.getLogger(DefaultSermoDialogExecutor.class);

    private final SermoStateMachineService<S, E> sermoStateMachineService;
    private final OutputHandler<S, E> outputHandler;
    private final CompositeSermoDialogListener<E> compositeListener;

    public DefaultSermoDialogExecutor(StateMachineFactory<S, E> stateMachineFactory, CachePersist<S, E> cachePersist) {
        this(new DefaultSermoStateMachineService<S, E>(stateMachineFactory, cachePersist, cachePersist));
    }

    public DefaultSermoDialogExecutor(StateMachineFactory<S, E> stateMachineFactory, StateMachinePersist<S, E, String> stateMachinePersist, StateMachineDeleter<String> stateMachineDeleter) {
        this(new DefaultSermoStateMachineService<S, E>(stateMachineFactory, stateMachinePersist, stateMachineDeleter));
    }

    public DefaultSermoDialogExecutor(SermoStateMachineService<S, E> sermoStateMachineService) {
        this(sermoStateMachineService, new DefaultOutputHandler<>());
    }

    public DefaultSermoDialogExecutor(SermoStateMachineService<S, E> sermoStateMachineService, OutputHandler<S, E> outputHandler) {
        this.sermoStateMachineService = sermoStateMachineService;
        this.outputHandler = outputHandler;
        this.compositeListener = new CompositeSermoDialogListener<>();
    }

    public DialogEventResult applyEvent(String sessionId, E event) throws SermoDialogException {
        StateMachine<S, E> stateMachine = null;
        DialogEventResult dialogEventResult = null;
        SermoDialogException exceptionThrown = null;

        notifyPreEventHandled(sessionId, event);
        try {
            stateMachine = acquireStateMachine(sessionId);
            dialogEventResult = handleEvent(stateMachine, event);
        } catch(Exception e) {
            exceptionThrown = toSermoDialogException(e);
            throw exceptionThrown;
        } finally {
            try {
                if (stateMachine != null) {
                    releaseStateMachine(stateMachine, sessionId, exceptionThrown != null);
                }
            } finally {
                notifyPostEventHandled(sessionId, event, exceptionThrown);
            }
        }
        return dialogEventResult;
    }

    public void register(SermoDialogListener<E> listener) {
        compositeListener.register(listener);
    }

    public void unregister(SermoDialogListener<E> listener) {
        compositeListener.unregister(listener);
    }

    protected StateMachine<S, E> acquireStateMachine(String sessionId) {
        try {
            return sermoStateMachineService.acquireStateMachine(sessionId);
        } catch(RuntimeException e) {
            throw new SermoDialogServiceException("Exception when acquiring a state machine", e);
        }
    }

    protected void releaseStateMachine(StateMachine<S, E> stateMachine, String sessionId, boolean exceptionThrown) {
        if (exceptionThrown) {
            sermoStateMachineService.releaseStateMachineOnException(sessionId, stateMachine);
        } else {
            sermoStateMachineService.releaseStateMachine(sessionId, stateMachine);
        }
    }

    protected DialogEventResult handleEvent(StateMachine<S, E> stateMachine, E event) {
        log.debug("Handling event: event={}, state={}", event, stateMachine.getState().getId());
        stateMachine.sendEvent(event);

        DialogEventResult dialogEventResult;
        if (stateMachine.hasStateMachineError()) {
            Optional<Exception> exOpt = ExtendedStateSupport.getExecutionException(stateMachine.getExtendedState());
            if(exOpt.isPresent()) {
                throw new SermoDialogExecutionException("State machine action exception", exOpt.get());
            } else {
                throw new SermoDialogExecutionException("State machine error (cause unknown)");
            }
        } else {
            boolean dialogComplete = stateMachine.isComplete();
            String output = outputHandler.getOutput(stateMachine);
            dialogEventResult = new DialogEventResult(output, dialogComplete);
            log.debug("Result of handling event: eventResult={}", dialogEventResult);
        }
        return dialogEventResult;
    }

    protected void notifyPreEventHandled(String sessionId, E event) {
        compositeListener.preEventHandled(sessionId, event);
    }

    protected void notifyPostEventHandled(String sessionId, E event, SermoDialogException e) {
        compositeListener.postEventHandled(sessionId, event, e);
    }

    protected SermoDialogException toSermoDialogException(Exception e) {
        if (e instanceof SermoDialogException) {
            return (SermoDialogException) e;
        } else {
            return new SermoDialogServiceException(e);
        }
    }

}
