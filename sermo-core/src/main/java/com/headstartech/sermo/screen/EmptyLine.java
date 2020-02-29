package com.headstartech.sermo.screen;

import java.util.StringJoiner;

/**
 * @author Per Johansson
 */
public class EmptyLine  implements ScreenBlock {

    public static final EmptyLine INSTANCE = new EmptyLine();

    private EmptyLine() {}

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EmptyLine.class.getSimpleName() + "[", "]")
                .toString();
    }
}
