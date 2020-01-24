package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class EmptyLine  implements ScreenBlock {

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }
}
