/*
 *  Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
        extendedState.getVariables().put(SermoSystemConstants.OUTPUT_KEY, output);
    }

    public static void setScreenMenuInputMap(ExtendedState extendedState, InputMap inputMap) {
        extendedState.getVariables().put(SermoSystemConstants.INPUT_MAP_KEY, inputMap);
    }


    public static InputMap getScreenMenuInputMap(ExtendedState extendedState) {
        return (InputMap) extendedState.getVariables().get(SermoSystemConstants.INPUT_MAP_KEY);
    }

    public static Optional<Object> getTransition(ExtendedState extendedState, String input) {
        InputMap inputMap = getScreenMenuInputMap(extendedState);
        if(inputMap != null) {
            return inputMap.getTransition(input);
        }
        return Optional.empty();
    }

    public static void setPagedScreenSetup(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup) {
        extendedState.getVariables().put(SermoSystemConstants.PAGED_SCREEN_KEY, pagedScreenSetup);
    }

    public static PagedScreenSetup getPagedScreenSetup(ExtendedState extendedState) {
        return (PagedScreenSetup) extendedState.getVariables().get(SermoSystemConstants.PAGED_SCREEN_KEY);
    }

    public static Object getItemData(ExtendedState extendedState) {
        return extendedState.getVariables().get(SermoSystemConstants.INPUT_ITEM_DATA_KEY);
    }

    public static InputMap getInputMap(ExtendedState extendedState) {
        return extendedState.get(SermoSystemConstants.INPUT_MAP_KEY, InputMap.class);
    }
}
