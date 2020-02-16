package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public interface StateMachineDeleter<T> {

    void delete(T contextObj);
}
