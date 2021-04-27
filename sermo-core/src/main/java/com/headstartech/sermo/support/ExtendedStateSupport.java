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
import com.headstartech.sermo.screen.PagedScreenSetup;
import com.headstartech.sermo.screen.Screen;
import org.springframework.statemachine.ExtendedState;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class ExtendedStateSupport {

    /**
     * Sets the string output which will be returned in {@link com.headstartech.sermo.DialogEventResult}.
     *
     * @param extendedState the extended state
     * @param output the output
     */
    public static void setOutput(ExtendedState extendedState, String output) {
        extendedState.getVariables().put(SystemConstants.OUTPUT_KEY, output);
    }

    /**
     * Sets the screen output which will be returned in {@link com.headstartech.sermo.DialogEventResult}
     * and the screens input map.
     *
     * @param extendedState the extended state
     * @param screen the screen
     */
    public static void setScreen(ExtendedState extendedState, Screen screen) {
        setScreenMenuInputMap(extendedState, screen.getInputMap());
        setOutput(extendedState, screen.getOutput());
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @param inputMap the input map
     */
    public static void setScreenMenuInputMap(ExtendedState extendedState, InputMap inputMap) {
        extendedState.getVariables().put(SystemConstants.INPUT_MAP_KEY, inputMap);
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @return {@code true} if the input map was cleared, {@code false} otherwise
     */
    public static boolean clearScreenMenuInputMap(ExtendedState extendedState) {
        return extendedState.getVariables().remove(SystemConstants.INPUT_MAP_KEY) != null;
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @return the input map
     */
    public static InputMap getScreenMenuInputMap(ExtendedState extendedState) {
        return (InputMap) extendedState.getVariables().get(SystemConstants.INPUT_MAP_KEY);
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @param input the input
     * @return the transition associated with the input or {@code  Optional.empty()}
     */
    public static Optional<Object> getTransition(ExtendedState extendedState, String input) {
        InputMap inputMap = getScreenMenuInputMap(extendedState);
        if(inputMap != null) {
            return inputMap.getTransition(input);
        }
        return Optional.empty();
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @param pagedScreenSetup the paged screen setup
     */
    public static void setPagedScreenSetup(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup) {
        extendedState.getVariables().put(SystemConstants.PAGED_SCREEN_KEY, pagedScreenSetup);
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @return the paged screen setup
     */
    public static PagedScreenSetup getPagedScreenSetup(ExtendedState extendedState) {
        return (PagedScreenSetup) extendedState.getVariables().get(SystemConstants.PAGED_SCREEN_KEY);
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     */
    public static void removePagedScreenSetup(ExtendedState extendedState) {
        extendedState.getVariables().remove(SystemConstants.PAGED_SCREEN_KEY);
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @return the execution exception or {@code  Optional.empty()}
     */
    public static Optional<RuntimeException> getExecutionException(ExtendedState extendedState) {
        return Optional.ofNullable(extendedState.get(SystemConstants.EXECUTION_EXCEPTION_KEY, RuntimeException.class));
    }

    /**
     * Internal helper method used by the library.
     *
     * @param extendedState the extended state
     * @param executionException the exception
     */
    public static void setExecutionException(ExtendedState extendedState, Exception executionException) {
        extendedState.getVariables().put(SystemConstants.EXECUTION_EXCEPTION_KEY, executionException);
    }

}
