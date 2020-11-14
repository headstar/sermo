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

package com.headstartech.sermo.support;

import com.headstartech.sermo.SystemConstants;
import com.headstartech.sermo.screen.InputMap;
import com.headstartech.sermo.screen.PagedMenuSetup;
import com.headstartech.sermo.screen.Screen;
import org.springframework.statemachine.ExtendedState;

import java.util.Optional;

import static com.headstartech.sermo.SystemConstants.INPUT_ITEM_DATA_KEY;

/**
 * @author Per Johansson
 */
public class ExtendedStateSupport {

    public static void setOutput(ExtendedState extendedState, String output) {
        extendedState.getVariables().put(SystemConstants.OUTPUT_KEY, output);
    }

    public static void setScreenMenuInputMap(ExtendedState extendedState, InputMap inputMap) {
        extendedState.getVariables().put(SystemConstants.INPUT_MAP_KEY, inputMap);
    }

    public static void clearScreenMenuInputMap(ExtendedState extendedState) {
        extendedState.getVariables().remove(SystemConstants.INPUT_MAP_KEY);
    }

    public static InputMap getScreenMenuInputMap(ExtendedState extendedState) {
        return (InputMap) extendedState.getVariables().get(SystemConstants.INPUT_MAP_KEY);
    }

    public static Optional<Object> getTransition(ExtendedState extendedState, String input) {
        InputMap inputMap = getScreenMenuInputMap(extendedState);
        if(inputMap != null) {
            return inputMap.getTransition(input);
        }
        return Optional.empty();
    }

    public static void setPagedMenuSetup(ExtendedState extendedState, PagedMenuSetup pagedMenuSetup) {
        extendedState.getVariables().put(SystemConstants.PAGED_SCREEN_KEY, pagedMenuSetup);
    }

    public static PagedMenuSetup getPagedMenuSetup(ExtendedState extendedState) {
        return (PagedMenuSetup) extendedState.getVariables().get(SystemConstants.PAGED_SCREEN_KEY);
    }

    // TODO: should be removed
    public static Object getItemData(ExtendedState extendedState) {
        return extendedState.getVariables().get(SystemConstants.INPUT_ITEM_DATA_KEY);
    }

    // TODO: should be removed
    public static void setItemData(ExtendedState extendedState, Object itemData) {
        extendedState.getVariables().put(INPUT_ITEM_DATA_KEY, itemData);
    }

    public static void setScreen(ExtendedState extendedState, Screen screen) {
        setScreenMenuInputMap(extendedState, screen.getInputMap());
        setOutput(extendedState, screen.getOutput());
    }

    public static Optional<Exception> getExecutionException(ExtendedState extendedState) {
        return Optional.ofNullable(extendedState.get(SystemConstants.EXECUTION_EXCEPTION_KEY, RuntimeException.class));
    }

    public static void setExecutionException(ExtendedState extendedState, Exception executionException) {
        extendedState.getVariables().put(SystemConstants.EXECUTION_EXCEPTION_KEY, executionException);
    }

}
