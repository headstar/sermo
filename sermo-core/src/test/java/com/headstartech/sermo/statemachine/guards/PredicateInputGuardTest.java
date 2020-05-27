package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import static com.headstartech.sermo.TestUtils.createStateContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredicateInputGuardTest {

    @Test
    public void evaluatesToTrueIfPredicateIsTrue() {
        // given
        Guard<String, DialogEvent> guard = new PredicateInputGuard<>(t -> true);
        StateContext<String, DialogEvent> stateContext = createStateContext("1");

        // when
        boolean res = guard.evaluate(stateContext);

        // then
        assertTrue(res);
    }

    @Test
    public void evaluatesToFalseIfPatternDoesNotMatches() {
        // given
        Guard<String, DialogEvent> guard = new PredicateInputGuard<>(t -> false);
        StateContext<String, DialogEvent> stateContext = createStateContext("1");

        // when
        boolean res = guard.evaluate(stateContext);

        // then
        assertFalse(res);
    }
}
