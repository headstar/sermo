package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;

/**
 * @author Per Johansson
 */
public class ExtendedStateKeys {

    public static final Object INPUT_ITEM_KEY = new Object();

    public static final Object INPUT_MAP_KEY = new Object();

    public static final Object SUPPORT_KEY = new Object();
    public static final Object OUTPUT_KEY = new Object();

    public static final Object TRANSITION_KEY = new Object();

    public static final Object NEXT_PAGE_KEY = new Object();
    public static final Object PREVIOUS_PAGE_KEY = new Object();

    static InputMap getInputMap(ExtendedState extendedState) {
        return extendedState.get(INPUT_MAP_KEY, InputMap.class);
    }
}
