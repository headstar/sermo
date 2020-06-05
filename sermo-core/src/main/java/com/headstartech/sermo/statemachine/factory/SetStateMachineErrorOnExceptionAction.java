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
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * {@link Action} which will be executed if any application {@code Action} throws an exception.
 *
 * It sets the Exception as the state machine error (processing is finished).
 *
 * @author Per Johansson
 */
public class SetStateMachineErrorOnExceptionAction<S, E> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {
        Exception exception = context.getException();
        context.getStateMachine().setStateMachineError(exception);
        ExtendedStateSupport.setExecutionException(context.getExtendedState(), exception);
    }
}
