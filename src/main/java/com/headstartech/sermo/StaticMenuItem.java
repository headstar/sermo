package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class StaticMenuItem extends MenuItem implements ScreenBlock {

    private final String input;

    public StaticMenuItem(String input, String label, Object transitionKey, Object itemKey) {
        super(label, transitionKey, itemKey);
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }
}
