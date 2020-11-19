package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

public class AndGuardTest {

    @SuppressWarnings("unchecked")
    private final StateContext<Object, DialogEvent> sc = mock(StateContext.class);

    @Test
    public void throwsExceptionIfNoGuard() {
        // given
        assertThrows(IllegalArgumentException.class, () -> new AndGuard<>(Collections.emptyList()));

        // when
        // then
    }

    @Test
    public void returnsTrueIfAllTrue() {
        // given
        AndGuard<Object, DialogEvent> guard =  Guards.and((c) -> true, (c) -> true);

        // when
        boolean res = guard.evaluate(sc);

        // then
        assertTrue(res);
    }

    @Test
    public void returnsFalseIfOneFalse() {
        // given
        AndGuard<Object, DialogEvent> guard =  Guards.and((c) -> true, (c) -> true, (c) -> false);

        // when
        boolean res = guard.evaluate(sc);

        // then
        assertFalse(res);
    }
}
