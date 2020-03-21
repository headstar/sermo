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

import com.headstartech.sermo.statemachine.StateMachineDeleter;
import com.headstartech.sermo.support.CompositeSermoDialogListener;
import com.headstartech.sermo.support.DefaultSermoStateMachineService;
import com.headstartech.sermo.support.SermoStateMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;

/**
 * @author Per Johansson
 */
public class SermoDialogExecutor<S, E extends DialogEvent> {

    private static final Logger log = LoggerFactory.getLogger(SermoDialogExecutor.class);

    private final SermoStateMachineService<S, E> sermoStateMachineService;
    private final CompositeSermoDialogListener<E> compositeListener;

    public SermoDialogExecutor(StateMachineFactory stateMachineFactory, StateMachinePersist<S, E, String> stateMachinePersist, StateMachineDeleter<String> stateMachineDeleter) {
        this(new DefaultSermoStateMachineService<S, E>(stateMachineFactory, stateMachinePersist, stateMachineDeleter));
    }

    public SermoDialogExecutor(SermoStateMachineService<S, E> sermoStateMachineService) {
        this.sermoStateMachineService = sermoStateMachineService;
        this.compositeListener = new CompositeSermoDialogListener<>();
    }

    public DialogEventResult applyEvent(String sessionId, E event) throws SermoException {
        StateMachine<S, E> stateMachine = null;
        DialogEventResult dialogEventResult = null;
        boolean exceptionThrown = false;
        try {
            compositeListener.preEventHandled(sessionId, event);
            stateMachine = sermoStateMachineService.acquireStateMachine(sessionId);
            dialogEventResult = handleEvent(stateMachine, event);
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
                compositeListener.postEventHandled(sessionId, event, dialogEventResult);
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

    protected DialogEventResult handleEvent(StateMachine<S, E> stateMachine, E event) {
        log.debug("Handling event: event={}, state={}", event, stateMachine.getState().getId());
        stateMachine.sendEvent(event);

        DialogEventResult dialogEventResult;
        if(stateMachine.hasStateMachineError()) {
            dialogEventResult = DialogEventResult.ofApplicationError(getOutput(stateMachine));
        } else if(stateMachine.isComplete()) {
            dialogEventResult = DialogEventResult.ofApplicationCompleted(getOutput(stateMachine));
        } else {
            String output = handleOutputWhenNoStateMachineError(stateMachine);
            dialogEventResult = DialogEventResult.ofOutput(output);
        }
        log.debug("Result of handling event: eventResult={}", dialogEventResult);
        return dialogEventResult;
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
