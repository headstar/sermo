package com.headstartech.sermo.actions;

import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class SetFixedOutputOnError<S,E> extends SetOutputOnError<S, E> {

    private final String output;

    public SetFixedOutputOnError(String output) {
        this.output = output;
    }

    protected String getOutput(StateContext<S, E> context) {
        return output;
    }
}
