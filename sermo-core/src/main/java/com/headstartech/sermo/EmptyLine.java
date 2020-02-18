package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class EmptyLine  implements ScreenBlock {

    private EmptyLine INSTANCE = new EmptyLine();

    private EmptyLine() {}

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }


}
