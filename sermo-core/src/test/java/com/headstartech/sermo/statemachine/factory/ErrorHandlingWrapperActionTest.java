package com.headstartech.sermo.statemachine.factory;

import com.headstartech.sermo.SystemConstants;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.support.DefaultExtendedState;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class ErrorHandlingWrapperActionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void executesDelegateAction() {
        // given
        StateContext<String, String> sc = mock(StateContext.class);
        StateMachine<String, String> sm = mock(StateMachine.class);
        when(sc.getStateMachine()).thenReturn(sm);
        when(sm.hasStateMachineError()).thenReturn(false);

        Action<String, String> delegate = mock(Action.class);

        ErrorHandlingWrapperAction<String, String> action = new ErrorHandlingWrapperAction<>(delegate);

        // when
        action.execute(sc);

        // then
        verify(delegate).execute(sc);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void wontExecuteDelegateActionIfStateMachineHasError() {
        // given
        StateContext<String, String> sc = mock(StateContext.class);
        StateMachine<String, String> sm = mock(StateMachine.class);
        when(sc.getStateMachine()).thenReturn(sm);
        when(sm.hasStateMachineError()).thenReturn(true);

        Action<String, String> delegate = mock(Action.class);

        ErrorHandlingWrapperAction<String, String> action = new ErrorHandlingWrapperAction<>(delegate);

        // when
        action.execute(sc);

        // then
        verifyNoInteractions(delegate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void stateMachineErrorSetIfDelegateThrows() {
        // given
        ExtendedState extendedState = new DefaultExtendedState();
        StateContext<String, String> sc = mock(StateContext.class);
        StateMachine<String, String> sm = mock(StateMachine.class);
        when(sc.getStateMachine()).thenReturn(sm);
        when(sm.hasStateMachineError()).thenReturn(false);
        when(sc.getExtendedState()).thenReturn(extendedState);

        RuntimeException delegateActionException = new RuntimeException();
        Action<String, String> delegate = mock(Action.class);
        doThrow(delegateActionException).when(delegate).execute(sc);

        ErrorHandlingWrapperAction<String, String> action = new ErrorHandlingWrapperAction<>(delegate);

        // when
        action.execute(sc);

        // then
        verify(sm).setStateMachineError(same(delegateActionException));
        assertSame(delegateActionException, extendedState.getVariables().get(SystemConstants.EXECUTION_EXCEPTION_KEY));
    }

}
