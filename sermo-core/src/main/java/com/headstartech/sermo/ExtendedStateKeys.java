package com.headstartech.sermo;

import com.headstartech.sermo.screen.InputMap;
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

    public static final String MDC_MACHINE_ID_KEY = "machineId";

    public static InputMap getInputMap(ExtendedState extendedState) {
        return extendedState.get(INPUT_MAP_KEY, InputMap.class);
    }
}
