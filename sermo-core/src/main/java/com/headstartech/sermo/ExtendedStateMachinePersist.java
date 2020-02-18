package com.headstartech.sermo;

import org.springframework.statemachine.StateMachinePersist;

/**
 * @author Per Johansson
 */
public interface ExtendedStateMachinePersist<S, E, T> extends StateMachinePersist<S, E, T> {

    void delete(T contextObj);
}
