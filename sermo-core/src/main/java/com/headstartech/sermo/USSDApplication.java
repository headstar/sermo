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
import org.slf4j.MDC;
import org.springframework.statemachine.StateMachine;

import static com.headstartech.sermo.USSDSystemConstants.MDC_SESSION_ID_KEY;

/**
 * @author Per Johansson
 */
public class USSDApplication<S, E extends MOInput> {

    private static final Logger log = LoggerFactory.getLogger(USSDApplication.class);

    private final USSDStateMachineService<S, E> ussdStateMachineService;

    public USSDApplication(USSDStateMachineService<S, E> ussdStateMachineService) {
        this.ussdStateMachineService = ussdStateMachineService;
    }

    public EventResult applyEvent(String sessionId, E event) {
        StateMachine<S, E> stateMachine = null;
        EventResult eventResult;
        try {
            setMDC(sessionId);
            stateMachine = ussdStateMachineService.acquireStateMachine(sessionId);
            eventResult = handleEvent(stateMachine, event);
        } finally {
            clearMDC();
            if(stateMachine != null) {
                ussdStateMachineService.releaseStateMachine(sessionId, stateMachine);
            }
        }
        return eventResult;
    }

    protected void setMDC(String machineId) {
        MDC.put(MDC_SESSION_ID_KEY, machineId);
    }

    protected void clearMDC() {
        MDC.clear();
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
            stateMachine.getExtendedState().getVariables().put(USSDSystemConstants.LAST_OUTPUT_KEY, output);
        } else {
            String lastOutput = stateMachine.getExtendedState().get(USSDSystemConstants.LAST_OUTPUT_KEY, String.class);
            if (lastOutput != null) {
                log.debug("No output set for event, using last output: lastOutput=\n{}", lastOutput);
                output = lastOutput;
            }
        }
        stateMachine.getExtendedState().getVariables().remove(USSDSystemConstants.OUTPUT_KEY);
        return output;
    }

    protected String getOutput(StateMachine<S, E> stateMachine) {
        return stateMachine.getExtendedState().get(USSDSystemConstants.OUTPUT_KEY, String.class);
    }
}
