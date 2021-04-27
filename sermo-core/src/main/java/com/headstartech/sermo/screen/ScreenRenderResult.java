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

package com.headstartech.sermo.screen;

/**
 * The result from a {@link ScreenRenderer}.
 *
 *
 * @author Per Johansson
 */
public class ScreenRenderResult {

    private final InputMap inputMap;
    private final String output;

    public ScreenRenderResult(InputMap inputMap, String output) {
        this.inputMap = inputMap;
        this.output = output;
    }

    /**
     * Gets the {@link InputMap} associated with the result.
     *
     * @return the input map
     */
    public InputMap getInputMap() {
        return inputMap;
    }

    /**
     * Gets the output.
     *
     * @return the output
     */
    public String getOutput() {
        return output;
    }
}
