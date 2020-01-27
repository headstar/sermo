package com.headstartech.sermo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Per Johansson
 */
public class Screen {

    private final Map<String, Object> inputTransitionKeyMap;
    private final Map<String, Object> inputItemKeyMap;
    private final String output;

    private Screen(Map<String, Object> inputTransitionKeyMap, Map<String, Object> inputItemKeyMap, String output) {
        this.inputTransitionKeyMap = inputTransitionKeyMap;
        this.inputItemKeyMap = inputItemKeyMap;
        this.output = output;
    }

    public Map<String, Object> getInputTransitionKeyMap() {
        return inputTransitionKeyMap;
    }

    public Map<String, Object> getInputItemKeyMap() {
        return inputItemKeyMap;
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
            ScreenRenderer screenBlockVisitor = new DefaultScreenRenderer();
            screenBlocks.forEach(e -> e.accept(screenBlockVisitor));
            return new Screen(screenBlockVisitor.getInputTransitionKeyMap(), screenBlockVisitor.getInputItemKeyMap(), screenBlockVisitor.getScreenOutput());
        }
    }

}
