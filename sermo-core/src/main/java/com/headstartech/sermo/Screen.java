package com.headstartech.sermo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Per Johansson
 */
public class Screen {

    private final InputMap inputMap;
    private final String output;

    private Screen(InputMap inputMap, String output) {
        this.inputMap = inputMap;
        this.output = output;
    }

    public InputMap getInputMap() {
        return inputMap;
    }

    public String getOutput() {
        return output;
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
            screenBlocks.forEach(e -> e.accept(renderer));
            return new Screen(renderer.getInputMap(), renderer.getScreenOutput());
        }
    }

}
