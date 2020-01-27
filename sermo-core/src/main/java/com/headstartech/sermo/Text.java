package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class Text implements ScreenBlock {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return text;
    }
}
