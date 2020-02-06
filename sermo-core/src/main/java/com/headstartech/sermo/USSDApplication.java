package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;

/**
 * @author Per Johansson
 */
public class USSDApplication<S, E> {

    private final StateMachine<S, E> sm;

    public USSDApplication(StateMachine<S, E> sm) {
        this.sm = sm;
    }

    public void start() {
        sm.start();
    }

    public String applyEvent(E event) {
        sm.sendEvent(event);
        String output = sm.getExtendedState().get(ExtendedStateKeys.OUTPUT_KEY, String.class);
        sm.getExtendedState().getVariables().remove(ExtendedStateKeys.OUTPUT_KEY);
        return output;
    }
}
