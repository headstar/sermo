package com.headstartech.sermo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

/**
 * @author Per Johansson
 */
public class USSDApplication<S, E extends MOInput> {

    private static final Log log = LogFactory.getLog(USSDApplication.class);

    private final USSDStateMachineService<S, E> ussdStateMachineService;

    public USSDApplication(USSDStateMachineService<S, E> ussdStateMachineService) {
        this.ussdStateMachineService = ussdStateMachineService;
    }

    public EventResult applyEvent(String machineId, E event) {
        StateMachine<S, E> stateMachine = null;
        EventResult eventResult = null;
        StateMachineListener<S, E> transitionListener = null;
        try {
            stateMachine = ussdStateMachineService.acquireStateMachine(machineId);
            transitionListener = new TransitionListener<>(stateMachine);
            stateMachine.addStateListener(transitionListener);
            stateMachine.sendEvent(event);
            String output = stateMachine.getExtendedState().get(ExtendedStateKeys.OUTPUT_KEY, String.class);
            if(!stateMachine.hasStateMachineError()) {
                if (output != null) {
                    stateMachine.getExtendedState().getVariables().put(ExtendedStateKeys.LAST_OUTPUT_KEY, output);
                } else {
                    String lastOutput = stateMachine.getExtendedState().get(ExtendedStateKeys.LAST_OUTPUT_KEY, String.class);
                    if (lastOutput != null) {
                        log.debug("No output set for event, using last output: machineId=" + machineId + ", lastOutput=\n" + lastOutput);
                        output = lastOutput;
                    }
                }
                stateMachine.getExtendedState().getVariables().remove(ExtendedStateKeys.OUTPUT_KEY);

                if(stateMachine.isComplete()) {
                    eventResult = EventResult.ofApplicationCompleted(output);
                } else {
                    eventResult = EventResult.ofOutput(output);
                }
            } else {
                eventResult = EventResult.ofApplicationError(output);
            }
        } finally {
            if(stateMachine != null) {
                if(transitionListener != null) {
                    stateMachine.removeStateListener(transitionListener);
                }
                ussdStateMachineService.releaseStateMachine(machineId, stateMachine);
            }
        }
        return eventResult;
    }

    private static class TransitionListener<S, E> extends StateMachineListenerAdapter<S, E> {

        private final StateMachine<S, E> stateMachine;

        public TransitionListener(StateMachine<S, E> stateMachine) {
            this.stateMachine = stateMachine;
        }

        @Override
        public void transition(Transition<S, E> transition) {
            log.debug("Transition triggered, clearing last output");
            stateMachine.getExtendedState().getVariables().remove(ExtendedStateKeys.LAST_OUTPUT_KEY);
        }
    }
}
