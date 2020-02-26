package com.headstartech.sermo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

/**
 * @author Per Johansson
 */
public class USSDApplication<S, E extends MOInput> {

    private static final Logger log = LoggerFactory.getLogger(USSDApplication.class);

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
            eventResult = handleEvent(stateMachine, machineId, event);
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

    protected EventResult handleEvent(StateMachine<S, E> stateMachine, String machineId, E event) {
        EventResult eventResult;

        stateMachine.sendEvent(event);
        if(!stateMachine.hasStateMachineError()) {
            String output = handleOutputWhenNoStateMachineError(stateMachine, machineId);
            if(stateMachine.isComplete()) {
                eventResult = EventResult.ofApplicationCompleted(output);
            } else {
                eventResult = EventResult.ofOutput(output);
            }
        } else {
            eventResult = EventResult.ofApplicationError(getOutput(stateMachine));
        }
        return eventResult;
    }

    protected String handleOutputWhenNoStateMachineError(StateMachine<S, E> stateMachine, String machineId) {
        String output = getOutput(stateMachine);
        if (output != null) {
            stateMachine.getExtendedState().getVariables().put(ExtendedStateKeys.LAST_OUTPUT_KEY, output);
        } else {
            String lastOutput = stateMachine.getExtendedState().get(ExtendedStateKeys.LAST_OUTPUT_KEY, String.class);
            if (lastOutput != null) {
                log.debug("No output set for event, using last output: machineId={}, lastOutput=\n{}", machineId, lastOutput);
                output = lastOutput;
            }
        }
        stateMachine.getExtendedState().getVariables().remove(ExtendedStateKeys.OUTPUT_KEY);
        return output;
    }

    protected String getOutput(StateMachine<S, E> stateMachine) {
        return stateMachine.getExtendedState().get(ExtendedStateKeys.OUTPUT_KEY, String.class);
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
