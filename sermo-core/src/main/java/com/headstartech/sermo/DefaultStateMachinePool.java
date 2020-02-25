package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

/**
 * @author Per Johansson
 */
public class DefaultStateMachinePool<S, E> implements StateMachinePool<S,E> {

    private final StateMachineFactory<S, E> stateMachineFactory;
    private final StateMachine<S, E> stateMachine;

    public DefaultStateMachinePool(StateMachineFactory<S, E> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
        this.stateMachine = stateMachineFactory.getStateMachine();
    }

    @Override
    public StateMachine<S, E> getStateMachine() {
        return stateMachine;
    }

    @Override
    public void returnStateMachine(StateMachine<S, E> stateMachine) {
        // do nothing
    }
}
