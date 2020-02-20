package com.headstartech.sermo.screen;

/**
 * @author Per Johansson
 */
public interface ScreenRenderer extends ScreenBlockVisitor {

    InputMap getInputMap();

    String getScreenOutput();

}
