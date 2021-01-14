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

package com.headstartech.sermo.statemachine.factory;

import com.headstartech.sermo.support.ExtendedStateSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * {@link Action} wrapping application {@link Action}s.
 *
 *  Sets state machine error state if application Action throws an exception. The application exception will be thrown out of {@link }com.headstartech.sermo.DialogExecutor#applyEvent(java.lang.String, com.headstartech.sermo.DialogEvent).
 *
 * @param <S>
 * @param <E>
 */
public class ErrorHandlingWrapperAction<S, E> implements Action<S, E> {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingWrapperAction.class);

    private final Action<S, E> delegate;

    public ErrorHandlingWrapperAction(Action<S, E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(StateContext<S, E> context) {
        if(context.getStateMachine().hasStateMachineError()) {
            log.debug("Statemachine has error, won't execute Action");
            return;
        }
        try {
            delegate.execute(context);
        } catch(RuntimeException e) {
            context.getStateMachine().setStateMachineError(e);
            ExtendedStateSupport.setExecutionException(context.getExtendedState(), e);
        }
    }
}
