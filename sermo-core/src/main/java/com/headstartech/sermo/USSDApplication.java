package com.headstartech.sermo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.transition.Transition;

/**
 * @author Per Johansson
 */
public class USSDApplication<S, E> {

    private static final Log log = LogFactory.getLog(USSDApplication.class);

    private final StateMachineService<S, E> stateMachineService;
    private final StateMachinePersister<S, E, String> stateMachinePersister;
    private final ExtendedStateMachinePersist<S, E, String> stateMachinePersist;

    public USSDApplication(StateMachineService<S, E> stateMachineService, StateMachinePersister<S, E, String> stateMachinePersister, ExtendedStateMachinePersist<S, E, String> stateMachinePersist) {
        this.stateMachineService = stateMachineService;
        this.stateMachinePersister = stateMachinePersister;
        this.stateMachinePersist = stateMachinePersist;
    }

    public USSDApplication(StateMachineFactory<S, E> stateMachineFactory, ExtendedStateMachinePersist<S, E, String> stateMachinePersist) {
        this(new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist), new DefaultStateMachinePersister<>(stateMachinePersist), stateMachinePersist);
    }

    public EventResult applyEvent(String machineId, E event) throws Exception {
        StateMachine<S, E> stateMachine = null;
        EventResult eventResult = null;
        try {
            stateMachine = stateMachineService.acquireStateMachine(machineId);
            // TODO: listener added multiple times to the state machine instance
            stateMachine.addStateListener(new TransitionListener<>(stateMachine));
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
            // releaseStateMachine() call below stops machine -> stateMachine.isComplete() = true
            boolean isCompleteOrHasStateMachineError = stateMachine.isComplete() || stateMachine.hasStateMachineError();
            stateMachineService.releaseStateMachine(machineId);
            if(stateMachine != null) {
                if(isCompleteOrHasStateMachineError) {
                    stateMachinePersist.delete(machineId);
                } else {
                    stateMachinePersister.persist(stateMachine, machineId);
                }
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
