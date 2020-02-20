package com.headstartech.sermo.screen;

import java.util.Objects;

/**
 * @author Per Johansson
 */
public class Text implements ScreenBlock {
    private final String text;

    public Text(String text) {
        Objects.requireNonNull(text, "text must be non-null");
        this.text = text;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text1 = (Text) o;
        return text.equals(text1.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
