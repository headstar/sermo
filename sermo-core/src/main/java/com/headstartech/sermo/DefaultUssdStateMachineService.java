package com.headstartech.sermo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineException;

/**
 * @author Per Johansson
 */
public class DefaultUssdStateMachineService<S, E extends MOInput> implements USSDStateMachineService<S, E> {

    private static final Logger log = LoggerFactory.getLogger(DefaultUssdStateMachineService.class);

    private final StateMachinePool<S, E> stateMachinePool;
    private final ExtendedStateMachinePersister<S, E, String> stateMachinePersister;

    public DefaultUssdStateMachineService(StateMachinePool<S, E> stateMachinePool, ExtendedStateMachinePersister<S, E, String> stateMachinePersister) {
        this.stateMachinePool = stateMachinePool;
        this.stateMachinePersister = stateMachinePersister;
    }

    @Override
    public StateMachine<S, E> acquireStateMachine(String machineId) {
        log.info("Acquiring state machine");
        StateMachine<S, E> stateMachine = stateMachinePool.getStateMachine();
        try {
            log.info("Restoring state machine machine state: machineId={}", machineId);
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
        } finally {
            stateMachinePool.returnStateMachine(stateMachine);
        }
    }

}