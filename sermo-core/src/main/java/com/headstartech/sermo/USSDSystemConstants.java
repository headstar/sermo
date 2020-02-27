package com.headstartech.sermo;

import com.headstartech.sermo.screen.InputMap;
import org.springframework.statemachine.ExtendedState;

/**
 * @author Per Johansson
 */
public class USSDSystemConstants {

    private USSDSystemConstants() {}

    public static final String INPUT_ITEM_DATA_KEY = "__input_item_data__";

    public static final String INPUT_MAP_KEY = "__input_map__";

    public static final String OUTPUT_KEY = "__output__";
    public static final String LAST_OUTPUT_KEY = "__last_output__";

    public static final String NEXT_PAGE_KEY = "__next_page__";
    public static final String PREVIOUS_PAGE_KEY = "__previous_page__";

    public static final String PAGED_SCREEN_KEY = "__paged_screen__";

    public static final String MDC_MACHINE_ID_KEY = "machineId";

}
