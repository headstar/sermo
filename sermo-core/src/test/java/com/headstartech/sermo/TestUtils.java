package com.headstartech.sermo;

import com.headstartech.sermo.screen.InputMap;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateContext;

public class TestUtils {

    private TestUtils() {}


    public static StateContext<String, DialogEvent> createStateContext(String input) {
        return new DefaultStateContext<>(null, new GenericMessage<>(new DialogEvent(input)), null, new DefaultExtendedState(),
                null, null, null, null, null);
    }

    public static StateContext<String, DialogEvent> createStateContext(String input, InputMap inputMap) {
        StateContext<String, DialogEvent> stateContext = TestUtils.createStateContext(input);
        ExtendedStateSupport.setScreenMenuInputMap(stateContext.getExtendedState(), inputMap);
        return stateContext;
    }
}
