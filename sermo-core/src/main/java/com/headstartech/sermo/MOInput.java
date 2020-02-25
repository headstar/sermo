package com.headstartech.sermo;

import org.springframework.statemachine.trigger.TriggerContext;

/**
 * @author Per Johansson
 */
public class MOInput {

    public static final MOInput INSTANCE = new MOInput();

    protected final String input;

    public MOInput() {
        input = null;
    }

    public MOInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public final int hashCode() {
        return 17;
    }

    /**
     * Sending any event extending this class to the state machine will result in {@link org.springframework.statemachine.trigger.EventTrigger#evaluate(TriggerContext)} returning true
     * and configured {@link org.springframework.statemachine.guard.Guard}s will control if any transition will be executed.
     *
     */
    @Override
    public final boolean equals(Object o) {
        return MOInput.class.isAssignableFrom(o.getClass());
    }

    @Override
    public String toString() {
        return "MOInput [input=" + input + "]";
    }
}
