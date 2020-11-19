package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class OrGuardTest {

    @SuppressWarnings("unchecked")
    private final StateContext<Object, DialogEvent> sc = mock(StateContext.class);

    @Test
    public void throwsExceptionIfNoGuard() {
        // given
        assertThrows(IllegalArgumentException.class, () -> new OrGuard<>(Collections.emptyList()));

        // when
        // then
    }

    @Test
    public void returnsTrueIfOneTrue() {
        // given
        OrGuard<Object, DialogEvent> guard =  Guards.or((c) -> false, (c) -> true);

        // when
        boolean res = guard.evaluate(sc);

        // then
        assertTrue(res);
    }

    @Test
    public void returnsFalseIfAllFalse() {
        // given
        OrGuard<Object, DialogEvent> guard =  Guards.or((c) -> false, (c) -> false, (c) -> false);

        // when
        boolean res = guard.evaluate(sc);

        // then
        assertFalse(res);
    }
}
