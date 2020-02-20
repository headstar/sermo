package com.headstartech.sermo;

import org.springframework.statemachine.persist.StateMachinePersister;

/**
 * @author Per Johansson
 */
public interface ExtendedStateMachinePersister<S, E extends MOInput, String> extends StateMachinePersister<S, E, String> {

    void delete(String contextObj) throws Exception;
}
