package com.headstartech.sermo.screen;

/**
 * @author Per Johansson
 */
public interface ScreenBlock {

    void accept(ScreenBlockVisitor visitor);
}
