package com.headstartech.sermo;

import org.springframework.statemachine.StateMachine;

/**
 * @author Per Johansson
 */
public interface StateMachineDeleter<T> {

    void delete(T contextObj) throws Exception;
}
