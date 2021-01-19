package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.regex.Pattern;

import static com.headstartech.sermo.TestUtils.createStateContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegExpTransitionGuardTest {

    @Test
    public void evaluatesToTrueIfPatternMatches() {
        // given
        Pattern pattern = Pattern.compile("a.c");
        RegExpTransitionGuard<String, DialogEvent> guard = Guards.regExp(pattern);
        StateContext<String, DialogEvent> stateContext = createStateContext("abc");

        // when
        boolean res = guard.evaluate(stateContext);

        // then
        assertTrue(res);
    }

    @Test
    public void evaluatesToFalseIfPatternDoesNotMatches() {
        // given
        Pattern pattern = Pattern.compile("a.c");
        RegExpTransitionGuard<String, DialogEvent> guard = Guards.regExp(pattern);
        StateContext<String, DialogEvent> stateContext = createStateContext("c");

        // when
        boolean res = guard.evaluate(stateContext);

        // then
        assertFalse(res);
    }
}
