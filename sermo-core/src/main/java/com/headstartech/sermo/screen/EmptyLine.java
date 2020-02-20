package com.headstartech.sermo.screen;

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


}
