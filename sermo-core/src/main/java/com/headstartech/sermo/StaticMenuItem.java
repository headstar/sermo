package com.headstartech.sermo;

import java.util.Objects;

/**
 * @author Per Johansson
 */
public class StaticMenuItem extends MenuItem implements ScreenBlock {

    private final String input;

    public StaticMenuItem(String input, String label, Object transitionKey) {
        super(label, transitionKey);
        Objects.requireNonNull(input, "input must be non-null");
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StaticMenuItem that = (StaticMenuItem) o;
        return input.equals(that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), input);
    }
}
