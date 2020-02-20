package com.headstartech.sermo.screen;

import com.headstartech.sermo.DefaultScreenRenderer;
import com.headstartech.sermo.InputMap;

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

        public Builder withScreenBlock(ScreenBlock screenBlock) {
            if(screenBlock != null) {
                screenBlocks.add(screenBlock);
            }
            return this;
        }

        public Screen build() {
            ScreenRenderer renderer = new DefaultScreenRenderer();
            ScreenBlocksContainer screenBlocksContainer = new ScreenBlocksContainer(screenBlocks);
            screenBlocksContainer.accept(renderer);
            return new Screen(renderer.getInputMap(), renderer.getScreenOutput(), screenBlocksContainer);
        }
    }

}
