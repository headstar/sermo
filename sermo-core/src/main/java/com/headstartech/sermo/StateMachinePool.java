package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;

/**
 * @author Per Johansson
 */
public interface StateMachinePool<S, E> {

    StateMachine<S, E> getStateMachine();

    void returnStateMachine(StateMachine<S,E> stateMachine);
}
