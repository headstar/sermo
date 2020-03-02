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

import com.headstartech.sermo.DefaultScreenRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Per Johansson
 */
public class Screen {

    private final InputMap inputMap;
    private final String output;
    private final ScreenBlocksContainer screenBlocksContainer;

    private Screen(InputMap inputMap, String output, ScreenBlocksContainer screenBlocksContainer) {
        this.inputMap = inputMap;
        this.output = output;
        this.screenBlocksContainer = screenBlocksContainer;
    }

    public InputMap getInputMap() {
        return inputMap;
    }

    public String getOutput() {
        return output;
    }

    public ScreenBlocksContainer getScreenBlocksContainer() {
        return screenBlocksContainer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<ScreenBlock> screenBlocks = new ArrayList<>();
        private ScreenRenderer screenRenderer;

        public Builder withScreenBlock(ScreenBlock screenBlock) {
            if(screenBlock != null) {
                screenBlocks.add(screenBlock);
            }
            return this;
        }

        public Builder withScreenRenderer(ScreenRenderer screenRenderer) {
            this.screenRenderer = screenRenderer;
            return this;
        }

        public Screen build() {
            ScreenRenderer renderer = screenRenderer == null ? new DefaultScreenRenderer() : screenRenderer;
            ScreenBlocksContainer screenBlocksContainer = new ScreenBlocksContainer(screenBlocks);
            screenBlocksContainer.accept(renderer);
            return new Screen(renderer.getInputMap(), renderer.getScreenOutput(), screenBlocksContainer);
        }
    }

}
