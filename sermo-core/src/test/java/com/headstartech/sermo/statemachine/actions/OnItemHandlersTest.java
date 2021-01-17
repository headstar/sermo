package com.headstartech.sermo.statemachine.actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.support.DefaultExtendedState;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnItemHandlersTest {

    @Test
    public void setsVariableInExtendedState() {
        // given
        String key = "a";
        String value = "b";
        OnItemHandler itemHandler = OnItemHandlers.setExtendedStateVariable(key, value);
        ExtendedState extendedState = new DefaultExtendedState();

        // when
        itemHandler.handle(extendedState);

        // then
        assertEquals(value, extendedState.get(key, String.class));
    }

    @Test
    public void canModifyVariableInExtendedState() {
        // given
        String key = "a";
        String value = "b";
        final AtomicBoolean consumerCalled = new AtomicBoolean(false);
        OnItemHandler itemHandler = OnItemHandlers.<String>modifyExtendedStateVariable(key, String.class, (e) -> {
            Assertions.assertEquals(value, e);
            consumerCalled.set(true);
        });
        ExtendedState extendedState = new DefaultExtendedState();
        extendedState.getVariables().put(key, value);

        // when
        itemHandler.handle(extendedState);

        // then
        assertTrue(consumerCalled.get());
    }

}
