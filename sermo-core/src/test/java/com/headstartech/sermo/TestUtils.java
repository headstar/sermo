package com.headstartech.sermo;

import com.headstartech.sermo.screen.InputMap;
import com.headstartech.sermo.states.USSDState;
import com.headstartech.sermo.states.USSDStates;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.guard.Guard;
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

    public static USSDState<States, DialogEvent> createState(States state, Action<States, DialogEvent> action) {
        return USSDStates.menuState(state, action);
    }

    public static USSDState<States, DialogEvent> createEndState(States state, Action<States, DialogEvent> action) {
        return USSDStates.endState(state, action);
    }

    public static Action<States, DialogEvent> nopAction() {
        return new NopAction<States, DialogEvent>();
    }

    public static enum States {
        A, B, C
    }

    public static class AlwaysTrueGuard<S, E> implements Guard<S, E> {
        @Override
        public boolean evaluate(StateContext<S, E> context) {
            return true;
        }
    }

    public static class NopAction<S, E> implements Action<S, E> {
        @Override
        public void execute(StateContext<S, E> context) {
        }
    }
}
