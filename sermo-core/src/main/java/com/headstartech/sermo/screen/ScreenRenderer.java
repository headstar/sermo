package com.headstartech.sermo.screen;

import com.headstartech.sermo.InputMap;

/**
 * @author Per Johansson
 */
public interface ScreenRenderer extends ScreenBlockVisitor {

    InputMap getInputMap();

    String getScreenOutput();

}
