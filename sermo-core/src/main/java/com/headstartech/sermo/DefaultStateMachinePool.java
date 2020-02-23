package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

/**
 * @author Per Johansson
 */
public class DefaultStateMachinePool<S, E> implements StateMachinePool<S,E> {

    private final StateMachineFactory<S, E> stateMachineFactory;

    public DefaultStateMachinePool(StateMachineFactory<S, E> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public StateMachine<S, E> getStateMachine() {
        return stateMachineFactory.getStateMachine();
    }

    @Override
    public void returnStateMachine(StateMachine<S, E> stateMachine) {
        // do nothing
    }
}
