package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class MOInput {

    public static final MOInput INSTANCE = new MOInput();

    private final String input;

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

    @Override
    public final boolean equals(Object o) {
        return MOInput.class.isAssignableFrom(o.getClass());
    }  // rely on guards

    @Override
    public String toString() {
        return "MOInput [input=" + input + "]";
    }
}
