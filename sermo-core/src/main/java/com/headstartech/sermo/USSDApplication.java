package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;

/**
 * @author Per Johansson
 */
public class USSDApplication {

    private final StateMachine<String, Object> sm;

    public USSDApplication(StateMachine<String, Object> sm) {
        this.sm = sm;
    }

    public void start() {
        sm.start();
    }

    public String applyEvent(Object event) {
        sm.sendEvent(event);
        String output = sm.getExtendedState().get(ExtendedStateKeys.OUTPUT_KEY, String.class);
        sm.getExtendedState().getVariables().remove(ExtendedStateKeys.OUTPUT_KEY);
        return output;
    }
}
