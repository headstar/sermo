package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public interface ScreenBlock {

    void accept(ScreenBlockVisitor visitor);
}
