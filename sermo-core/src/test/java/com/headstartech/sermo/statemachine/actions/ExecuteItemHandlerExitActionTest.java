package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.TestUtils;
import com.headstartech.sermo.screen.InputMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExecuteItemHandlerExitActionTest {

    @Test
    public void throwsExceptionIfNotOnItemHandler() {
        // given
        Action<String, DialogEvent> action = new ExecuteItemHandlerAction<>();

        InputMap inputMap = InputMap.builder().
                addMapping("1", "tr1", new Object())
                .build();
        StateContext<String, DialogEvent> stateContext = TestUtils.createStateContext("1", inputMap);

        // when
        Assertions.assertThrows(IllegalStateException.class,
                () -> action.execute(stateContext));
    }

    @Test
    public void callingItemHandler() {
        // given
        Action<String, DialogEvent> action = new ExecuteItemHandlerAction<>();

        OnItemHandler onItemHandler = Mockito.mock(OnItemHandler.class);

        InputMap inputMap = InputMap.builder().
                addMapping("1", "tr1", onItemHandler)
                .build();
        StateContext<String, DialogEvent> stateContext = TestUtils.createStateContext("1", inputMap);

        // when
        action.execute(stateContext);

        // then
        verify(onItemHandler).handle(stateContext.getExtendedState());
    }
}
