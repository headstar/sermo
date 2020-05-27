package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.InputMap;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import static com.headstartech.sermo.TestUtils.createStateContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScreenTransitionGuardTest {

    private static final String TRANSITION_ID = "trid";

    @Test
    public void evaluatesToTrueIfHavingTransitionForInput() {
        // given
        String input = "1";
        Guard<String, DialogEvent> guard = new ScreenTransitionGuard<>(TRANSITION_ID);
        InputMap inputMap = InputMap.builder().addMapping("1", TRANSITION_ID).build();
        StateContext<String, DialogEvent> sc = createStateContext(input, inputMap);
        // when
        boolean res = guard.evaluate(sc);

        // then
        assertTrue(res);
    }

    @Test
    public void evaluatesToFalseIfNotHavingTransitionForInput() {
        // given
        String input = "1";
        Guard<String, DialogEvent> guard = new ScreenTransitionGuard<>(TRANSITION_ID);
        InputMap inputMap = InputMap.builder().addMapping("2", TRANSITION_ID).build();
        StateContext<String, DialogEvent> sc = createStateContext(input, inputMap);

        // when
        boolean res = guard.evaluate(sc);

        // then
        assertFalse(res);
    }


}
