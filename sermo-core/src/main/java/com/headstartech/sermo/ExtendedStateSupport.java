package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class ExtendedStateSupport {

    public static void setOutput(ExtendedState extendedState, String output) {
        extendedState.getVariables().put(ExtendedStateKeys.OUTPUT_KEY, output);
    }

    public static void setScreenMenuInputMap(ExtendedState extendedState, InputMap inputMap) {
        extendedState.getVariables().put(ExtendedStateKeys.INPUT_MAP_KEY, inputMap);
    }


    public static InputMap getScreenMenuInputMap(ExtendedState extendedState) {
        return (InputMap) extendedState.getVariables().get(ExtendedStateKeys.INPUT_MAP_KEY);
    }

    public static Optional<Object> getTransition(ExtendedState extendedState, String input) {
        InputMap inputMap = getScreenMenuInputMap(extendedState);
        if(inputMap != null) {
            return inputMap.getTransition(input);
        }
        return Optional.empty();
    }

    public static void setPagedScreenSetup(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup) {
        extendedState.getVariables().put(ExtendedStateKeys.PAGED_SCREEN_KEY, pagedScreenSetup);
    }

    public static PagedScreenSetup getPagedScreenSetup(ExtendedState extendedState) {
        return (PagedScreenSetup) extendedState.getVariables().get(ExtendedStateKeys.PAGED_SCREEN_KEY);
    }

    public static Object getItemData(ExtendedState extendedState) {
        return extendedState.getVariables().get(ExtendedStateKeys.INPUT_ITEM_DATA_KEY);
    }

}
