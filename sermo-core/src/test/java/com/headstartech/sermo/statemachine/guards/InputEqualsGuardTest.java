package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import static com.headstartech.sermo.TestUtils.createStateContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputEqualsGuardTest {

    @Test
    public void returnsTrueIfInputEquals() {
        // given
        InputEqualsGuard<String, DialogEvent> guard = Guards.eq("1");
        StateContext<String, DialogEvent> stateContext = createStateContext("1");

        // when
        boolean res = guard.evaluate(stateContext);

        // then
        assertTrue(res);
    }

    @Test
    public void returnsFalseIfInputNotEquals() {
        // given
        InputEqualsGuard<String, DialogEvent> guard = Guards.eq("1");
        StateContext<String, DialogEvent> stateContext = createStateContext("2");

        // when
        boolean res = guard.evaluate(stateContext);

        // then
        assertFalse(res);
    }
}
