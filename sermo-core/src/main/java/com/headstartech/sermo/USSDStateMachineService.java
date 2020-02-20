package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;

/**
 * @author Per Johansson
 */
public interface USSDStateMachineService<S, E extends MOInput> {

    StateMachine<S, E> acquireStateMachine(String machineId);

    void releaseStateMachine(String machineId, StateMachine<S, E> stateMachine);

}
