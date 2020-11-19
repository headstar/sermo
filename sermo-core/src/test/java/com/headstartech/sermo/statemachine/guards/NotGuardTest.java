package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class NotGuardTest {

    @SuppressWarnings("unchecked")
    private final StateContext<Object, DialogEvent> sc = mock(StateContext.class);

    @Test
    public void returnsTrueIfDelegateIsFalse() {
        // given
        NotGuard<Object, DialogEvent> guard =  Guards.not((c) -> false);

        // when
        boolean res = guard.evaluate(sc);

        // then
        assertTrue(res);
    }

    @Test
    public void returnsFalseIfDelegateIsTrue() {
        // given
        NotGuard<Object, DialogEvent> guard =  Guards.not((c) -> true);

        // when
        boolean res = guard.evaluate(sc);

        // then
        assertFalse(res);
    }
}
