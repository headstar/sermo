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

    public String start() {
        sm.getExtendedState().getVariables().put(ExtendedStateKeys.SUPPORT_KEY, new DefaultUSSDSupport(sm.getExtendedState()));
        sm.start();
        String output = sm.getExtendedState().get(ExtendedStateKeys.OUTPUT_KEY, String.class);
        sm.getExtendedState().getVariables().remove(ExtendedStateKeys.OUTPUT_KEY);
        return output;
    }

    public String applyEvent(Object event) {
        sm.getExtendedState().getVariables().put(ExtendedStateKeys.SUPPORT_KEY, new DefaultUSSDSupport(sm.getExtendedState()));
        sm.sendEvent(event);
        String output = sm.getExtendedState().get(ExtendedStateKeys.OUTPUT_KEY, String.class);
        sm.getExtendedState().getVariables().remove(ExtendedStateKeys.OUTPUT_KEY);
        return output;
    }
}
