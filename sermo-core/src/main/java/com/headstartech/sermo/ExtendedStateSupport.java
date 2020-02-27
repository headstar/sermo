package com.headstartech.sermo;

import com.headstartech.sermo.screen.InputMap;
import com.headstartech.sermo.screen.PagedScreenSetup;
import org.springframework.statemachine.ExtendedState;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class ExtendedStateSupport {

    public static void setOutput(ExtendedState extendedState, String output) {
        extendedState.getVariables().put(USSDSystemConstants.OUTPUT_KEY, output);
    }

    public static void setScreenMenuInputMap(ExtendedState extendedState, InputMap inputMap) {
        extendedState.getVariables().put(USSDSystemConstants.INPUT_MAP_KEY, inputMap);
    }


    public static InputMap getScreenMenuInputMap(ExtendedState extendedState) {
        return (InputMap) extendedState.getVariables().get(USSDSystemConstants.INPUT_MAP_KEY);
    }

    public static Optional<Object> getTransition(ExtendedState extendedState, String input) {
        InputMap inputMap = getScreenMenuInputMap(extendedState);
        if(inputMap != null) {
            return inputMap.getTransition(input);
        }
        return Optional.empty();
    }

    public static void setPagedScreenSetup(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup) {
        extendedState.getVariables().put(USSDSystemConstants.PAGED_SCREEN_KEY, pagedScreenSetup);
    }

    public static PagedScreenSetup getPagedScreenSetup(ExtendedState extendedState) {
        return (PagedScreenSetup) extendedState.getVariables().get(USSDSystemConstants.PAGED_SCREEN_KEY);
    }

    public static Object getItemData(ExtendedState extendedState) {
        return extendedState.getVariables().get(USSDSystemConstants.INPUT_ITEM_DATA_KEY);
    }

    public static InputMap getInputMap(ExtendedState extendedState) {
        return extendedState.get(USSDSystemConstants.INPUT_MAP_KEY, InputMap.class);
    }
}
