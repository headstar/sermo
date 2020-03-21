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

/**
 * @author Per Johansson
 */
public class SermoDialogExecutor<S, E extends MOInput> {

    private static final Logger log = LoggerFactory.getLogger(SermoDialogExecutor.class);

    private final SermoStateMachineService<S, E> sermoStateMachineService;
    private final CompositeSermoDialogListener<E> ussdApplicationListener;

    public SermoDialogExecutor(SermoStateMachineService<S, E> sermoStateMachineService) {
        this.sermoStateMachineService = sermoStateMachineService;
        this.ussdApplicationListener = new CompositeSermoDialogListener<>();
    }

    public EventResult applyEvent(String sessionId, E event) throws SermoException {
        StateMachine<S, E> stateMachine = null;
        EventResult eventResult = null;
        boolean exceptionThrown = false;
        try {
            ussdApplicationListener.preEventHandled(sessionId, event);
            stateMachine = sermoStateMachineService.acquireStateMachine(sessionId);
            eventResult = handleEvent(stateMachine, event);
        } catch(RuntimeException e) {
            exceptionThrown = true;
            if(e instanceof SermoException) {
                throw e;
            } else {
                throw new SermoException(e);
            }
        } finally {
            try {
                if (stateMachine != null) {
                    if(exceptionThrown) {
                        sermoStateMachineService.releaseStateMachineOnException(sessionId, stateMachine);
                    } else {
                        sermoStateMachineService.releaseStateMachine(sessionId, stateMachine);
                    }
                }
            } finally {
                ussdApplicationListener.postEventHandled(sessionId, event, eventResult);
            }
        }
        return eventResult;
    }

    public void register(SermoDialogListener<E> listener) {
        ussdApplicationListener.register(listener);
    }

    public void unregister(SermoDialogListener<E> listener) {
        ussdApplicationListener.unregister(listener);
    }

    protected EventResult handleEvent(StateMachine<S, E> stateMachine, E event) {
        log.debug("Handling event: event={}, state={}", event, stateMachine.getState().getId());
        stateMachine.sendEvent(event);

        EventResult eventResult;
        if(stateMachine.hasStateMachineError()) {
            eventResult = EventResult.ofApplicationError(getOutput(stateMachine));
        } else if(stateMachine.isComplete()) {
            eventResult = EventResult.ofApplicationCompleted(getOutput(stateMachine));
        } else {
            String output = handleOutputWhenNoStateMachineError(stateMachine);
            eventResult = EventResult.ofOutput(output);
        }
        log.debug("Result of handling event: eventResult={}", eventResult);
        return eventResult;
    }

    protected String handleOutputWhenNoStateMachineError(StateMachine<S, E> stateMachine) {
        String output = getOutput(stateMachine);
        if (output != null) {
            stateMachine.getExtendedState().getVariables().put(SermoSystemConstants.LAST_OUTPUT_KEY, output);
        } else {
            String lastOutput = stateMachine.getExtendedState().get(SermoSystemConstants.LAST_OUTPUT_KEY, String.class);
            if (lastOutput != null) {
                log.debug("No output set for event, using last output: lastOutput=\n{}", lastOutput);
                output = lastOutput;
            }
        }
        stateMachine.getExtendedState().getVariables().remove(SermoSystemConstants.OUTPUT_KEY);
        return output;
    }

    protected String getOutput(StateMachine<S, E> stateMachine) {
        return stateMachine.getExtendedState().get(SermoSystemConstants.OUTPUT_KEY, String.class);
    }
}
