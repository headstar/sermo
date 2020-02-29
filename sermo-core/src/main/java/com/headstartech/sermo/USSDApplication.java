package com.headstartech.sermo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

import static com.headstartech.sermo.USSDSystemConstants.MDC_MACHINE_ID_KEY;

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
            setMDC(machineId);
            stateMachine = ussdStateMachineService.acquireStateMachine(machineId);
            transitionListener = new TransitionListener<>(stateMachine);
            stateMachine.addStateListener(transitionListener);
            eventResult = handleEvent(stateMachine, machineId, event);
        } finally {
            clearMDC();
            if(stateMachine != null) {
                if(transitionListener != null) {
                    stateMachine.removeStateListener(transitionListener);
                }
                ussdStateMachineService.releaseStateMachine(machineId, stateMachine);
            }
        }
        return eventResult;
    }

    protected void setMDC(String machineId) {
        MDC.put(MDC_MACHINE_ID_KEY, machineId);
    }

    protected void clearMDC() {
        MDC.clear();
    }

    protected EventResult handleEvent(StateMachine<S, E> stateMachine, String machineId, E event) {
        stateMachine.sendEvent(event);

        EventResult eventResult;
        if(stateMachine.hasStateMachineError()) {
            eventResult = EventResult.ofApplicationError(getOutput(stateMachine));
        } else if(stateMachine.isComplete()) {
            eventResult = EventResult.ofApplicationCompleted(getOutput(stateMachine));
        } else {
            String output = handleOutputWhenNoStateMachineError(stateMachine);
            eventResult = EventResult.ofOutput(output);
        }
        return eventResult;
    }

    protected String handleOutputWhenNoStateMachineError(StateMachine<S, E> stateMachine) {
        String output = getOutput(stateMachine);
        if (output != null) {
            stateMachine.getExtendedState().getVariables().put(USSDSystemConstants.LAST_OUTPUT_KEY, output);
        } else {
            String lastOutput = stateMachine.getExtendedState().get(USSDSystemConstants.LAST_OUTPUT_KEY, String.class);
            if (lastOutput != null) {
                log.debug("No output set for event, using last output: lastOutput=\n{}", lastOutput);
                output = lastOutput;
            }
        }
        stateMachine.getExtendedState().getVariables().remove(USSDSystemConstants.OUTPUT_KEY);
        return output;
    }

    protected String getOutput(StateMachine<S, E> stateMachine) {
        return stateMachine.getExtendedState().get(USSDSystemConstants.OUTPUT_KEY, String.class);
    }

    private static class TransitionListener<S, E> extends StateMachineListenerAdapter<S, E> {

        private final StateMachine<S, E> stateMachine;

        public TransitionListener(StateMachine<S, E> stateMachine) {
            this.stateMachine = stateMachine;
        }

        @Override
        public void transition(Transition<S, E> transition) {
            log.trace("Transition triggered, clearing last output");
            stateMachine.getExtendedState().getVariables().remove(USSDSystemConstants.LAST_OUTPUT_KEY);
        }
    }
}
