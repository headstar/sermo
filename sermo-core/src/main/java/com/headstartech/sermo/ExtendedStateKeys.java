package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;

/**
 * @author Per Johansson
 */
public class ExtendedStateKeys {

    public static final Object INPUT_ITEM_DATA_KEY = new Object();

    public static final Object INPUT_MAP_KEY = new Object();

    public static final Object OUTPUT_KEY = new Object();
    public static final Object LAST_OUTPUT_KEY = new Object();

    public static final Object NEXT_PAGE_KEY = new Object();
    public static final Object PREVIOUS_PAGE_KEY = new Object();

    public static final String PAGED_SCREEN_KEY = "pagedScreen";

    static InputMap getInputMap(ExtendedState extendedState) {
        return extendedState.get(INPUT_MAP_KEY, InputMap.class);
    }
}
