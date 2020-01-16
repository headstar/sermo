package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class MOInput {

    public static final MOInput INSTANCE = new MOInput();

    private final String input;

    private MOInput() {
        input = null;
    }

    public MOInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }  // rely on guards
}
