package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

/**
 * @author Per Johansson
 */
public class USSDApplication<S, E> {

    private final StateMachineService<S, E> stateMachineService;
    private final StateMachinePersister<S, E, String> stateMachinePersister;
    private final StateMachineDeleter<String> stateMachineDeleter;

    public USSDApplication(StateMachineService<S, E> stateMachineService, StateMachinePersister<S, E, String> stateMachinePersister, StateMachineDeleter<String> stateMachineDeleter) {
        this.stateMachineService = stateMachineService;
        this.stateMachinePersister = stateMachinePersister;
        this.stateMachineDeleter = stateMachineDeleter;
    }

    public USSDApplication(StateMachineFactory<S, E> stateMachineFactory, StateMachinePersist<S, E, String> stateMachinePersist, StateMachineDeleter<String> stateMachineDeleter) {
        this(new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist), new DefaultStateMachinePersister<>(stateMachinePersist),
                stateMachineDeleter);
    }

    public String applyEvent(String machineId, E event) throws Exception {
        StateMachine<S, E> stateMachine = null;
        boolean machineCompletedOrHasError = true;
        try {
            stateMachine = stateMachineService.acquireStateMachine(machineId);
            stateMachine.sendEvent(event);
            machineCompletedOrHasError = stateMachine.isComplete() || stateMachine.hasStateMachineError();
            String output = stateMachine.getExtendedState().get(ExtendedStateKeys.OUTPUT_KEY, String.class);
            stateMachine.getExtendedState().getVariables().remove(ExtendedStateKeys.OUTPUT_KEY);
            return output;
        } finally {
            stateMachineService.releaseStateMachine(machineId);
            if(stateMachine != null) {
                if(machineCompletedOrHasError) {
                    stateMachineDeleter.delete(machineId);
                } else {
                    stateMachinePersister.persist(stateMachine, machineId);
                }
            }
        }
    }
}
