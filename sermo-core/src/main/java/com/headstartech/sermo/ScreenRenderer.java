package com.headstartech.sermo;

import java.util.Map;

/**
 * @author Per Johansson
 */
public interface ScreenRenderer extends ScreenBlockVisitor {

    Map<String, Object> getInputTransitionKeyMap();

    Map<String, Object> getInputItemKeyMap();

    String getScreenOutput();

}
