package com.headstartech.sermo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.DefaultStateMachineService;

/**
 * @author Per Johansson
 */
public class DefaultUssdStateMachineService<S, E extends MOInput> implements USSDStateMachineService<S, E> {

    private final static Log log = LogFactory.getLog(DefaultStateMachineService.class);

    private final StateMachineFactory<S, E> stateMachineFactory;
    private final ExtendedStateMachinePersister<S, E, String> stateMachinePersister;

    public DefaultUssdStateMachineService(StateMachineFactory<S, E> stateMachineFactory, ExtendedStateMachinePersister<S, E, String> stateMachinePersister) {
        this.stateMachineFactory = stateMachineFactory;
        this.stateMachinePersister = stateMachinePersister;
    }

    @Override
    public StateMachine<S, E> acquireStateMachine(String machineId) {
        log.info("Acquiring machine with id " + machineId);
        StateMachine<S, E> stateMachine;

        // TODO: how to handle concurrency?

        log.info("Getting new machine from factory with id " + machineId);
        stateMachine = stateMachineFactory.getStateMachine(machineId);
        try {
            stateMachinePersister.restore(stateMachine, machineId);
        } catch (Exception e) {
            log.error("Error handling context", e);
            throw new StateMachineException("Unable to read context from store", e);
        }

        return stateMachine;
    }

    @Override
    public void releaseStateMachine(String machineId, StateMachine<S, E> stateMachine) {
        try {
            boolean isCompleteOrHasStateMachineError = stateMachine.isComplete() || stateMachine.hasStateMachineError();
            if(isCompleteOrHasStateMachineError) {
                stateMachinePersister.delete(machineId);
            } else {
                stateMachinePersister.persist(stateMachine, machineId);
            }
        } catch (Exception e) {
            log.error("Error handling context", e);
            throw new StateMachineException("Unable to persist context to store", e);
        }
    }

}