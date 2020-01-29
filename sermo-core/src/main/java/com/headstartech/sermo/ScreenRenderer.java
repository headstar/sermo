package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public interface ScreenRenderer extends ScreenBlockVisitor {

    InputMap getInputMap();

    String getScreenOutput();

}
