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

import com.headstartech.sermo.SystemConstants;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultExtendedState;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;


/**
 * @author Per Johansson
 */
public class SetStateMachineErrorOnExceptionActionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void stateMachineHasErrorSetAndExceptionInExtendedState() {
        // given
        SetStateMachineErrorOnExceptionAction<String, String> action = new SetStateMachineErrorOnExceptionAction<>();
        RuntimeException executionException = new RuntimeException();
        ExtendedState extendedState = new DefaultExtendedState();

        StateContext<String, String> sc = mock(StateContext.class);
        StateMachine<String, String> sm = mock(StateMachine.class);
        when(sc.getStateMachine()).thenReturn(sm);
        when(sc.getException()).thenReturn(executionException);
        when(sc.getExtendedState()).thenReturn(extendedState);

        // when
        action.execute(sc);

        // then
        verify(sm).setStateMachineError(same(executionException));
        assertSame(executionException, extendedState.getVariables().get(SystemConstants.EXECUTION_EXCEPTION_KEY));
    }

}
