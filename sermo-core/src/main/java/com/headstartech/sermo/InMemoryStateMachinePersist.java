package com.headstartech.sermo;

import org.springframework.statemachine.StateMachineContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Per Johansson
 */
public class InMemoryStateMachinePersist<S, E> implements ExtendedStateMachinePersist<S, E, String> {

    private final ConcurrentHashMap<String, StateMachineContext<S, E>> contexts = new ConcurrentHashMap<>();

    @Override
    public void write(StateMachineContext<S, E> context, String contextObj) throws Exception {
        contexts.put(contextObj, context);
    }

    @Override
    public StateMachineContext<S, E> read(String contextObj) throws Exception {
        return contexts.get(contextObj);
    }

    @Override
    public void delete(String contextObj) {
        contexts.remove(contextObj);
    }
}
