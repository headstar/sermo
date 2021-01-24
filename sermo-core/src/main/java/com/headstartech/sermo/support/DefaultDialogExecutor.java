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
import com.headstartech.sermo.DialogEventResult;
import com.headstartech.sermo.DialogExecutor;
import com.headstartech.sermo.DialogListener;
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
public class DefaultDialogExecutor<S, E extends DialogEvent> implements DialogExecutor<S, E> {

    private static final Logger log = LoggerFactory.getLogger(DefaultDialogExecutor.class);

    private final StateMachineService<S, E> stateMachineService;
    private final OutputHandler<S, E> outputHandler;
    private final CompositeDialogListener<E> compositeListener;

    public DefaultDialogExecutor(StateMachineFactory<S, E> stateMachineFactory, CachePersist<S, E> cachePersist) {
        this(new DefaultStateMachineService<>(stateMachineFactory, cachePersist, cachePersist));
    }

    public DefaultDialogExecutor(StateMachineFactory<S, E> stateMachineFactory, StateMachinePersist<S, E, String> stateMachinePersist, StateMachineDeleter<String> stateMachineDeleter) {
        this(new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist, stateMachineDeleter));
    }

    public DefaultDialogExecutor(StateMachineService<S, E> stateMachineService) {
        this(stateMachineService, new DefaultOutputHandler<>());
    }

    public DefaultDialogExecutor(StateMachineService<S, E> stateMachineService, OutputHandler<S, E> outputHandler) {
        this.stateMachineService = stateMachineService;
        this.outputHandler = outputHandler;
        this.compositeListener = new CompositeDialogListener<>();
    }

    public DialogEventResult applyEvent(String sessionId, E event) {
        StateMachine<S, E> stateMachine = null;
        DialogEventResult dialogEventResult = null;
        RuntimeException exceptionThrown = null;

        notifyPreEventHandled(sessionId, event);
        try {
            stateMachine = acquireStateMachine(sessionId);
            dialogEventResult = handleEvent(stateMachine, event);
        } catch(RuntimeException e) {
            exceptionThrown = e;
            throw e;
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

    public void addListener(DialogListener<E> listener) {
        compositeListener.register(listener);
    }

    public void removeListener(DialogListener<E> listener) {
        compositeListener.unregister(listener);
    }

    protected StateMachine<S, E> acquireStateMachine(String sessionId) {
        return stateMachineService.acquireStateMachine(sessionId);
    }

    protected void releaseStateMachine(StateMachine<S, E> stateMachine, String sessionId, boolean exceptionThrown) {
        if (exceptionThrown) {
            stateMachineService.releaseStateMachineOnException(sessionId, stateMachine);
        } else {
            stateMachineService.releaseStateMachine(sessionId, stateMachine);
        }
    }

    protected DialogEventResult handleEvent(StateMachine<S, E> stateMachine, E event) {
        log.debug("Handling event: event={}, state={}", event, stateMachine.getState().getId());
        stateMachine.sendEvent(event);

        DialogEventResult dialogEventResult;
        if (stateMachine.hasStateMachineError()) {
            Optional<RuntimeException> exOpt = ExtendedStateSupport.getExecutionException(stateMachine.getExtendedState());
            throw exOpt.orElseThrow(() -> new RuntimeException("State machine error (cause unknown)"));
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

    protected void notifyPostEventHandled(String sessionId, E event, RuntimeException e) {
        compositeListener.postEventHandled(sessionId, event, e);
    }

}
