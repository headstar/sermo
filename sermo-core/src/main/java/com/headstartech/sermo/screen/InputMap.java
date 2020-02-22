package com.headstartech.sermo.screen;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Per Johansson
 */
public class InputMap {

    private final Map<String, Object> inputTransitionMap;
    private final Map<String, Object> inputItemDataMap;

    public InputMap() {
        inputTransitionMap = new HashMap<>();
        inputItemDataMap = new HashMap<>();
    }

    public void addMapping(String input, Object transitionId, Object itemData) {
        inputTransitionMap.put(input, transitionId);
        inputItemDataMap.put(input, itemData);
    }

    public Optional<Object> getItemDataForInput(String input) {
        return Optional.ofNullable(inputItemDataMap.get(input));
    }

    public boolean hasTransitionForInput(Object transitionId, String input) {
        return transitionId.equals(inputTransitionMap.get(input));
    }

    public Optional<Object> getTransition(String input) {
        return Optional.ofNullable(inputTransitionMap.get(input));
    }
}
