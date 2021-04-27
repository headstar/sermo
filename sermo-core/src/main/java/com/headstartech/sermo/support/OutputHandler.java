package com.headstartech.sermo.support;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.StateMachine;

/**
 * Strategy for handling dialog output.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface OutputHandler<S, E extends DialogEvent> {

    /**
     * Called after an event has been handled and the {@link StateMachine} does not have an error.
     *
     * @param stateMachine
     * @return
     */
    String getOutput(StateMachine<S, E> stateMachine);

}
