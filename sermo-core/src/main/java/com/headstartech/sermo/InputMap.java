package com.headstartech.sermo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Per Johansson
 */
public class InputMap {

    private final Map<String, Object> inputTransitionNameMap;
    private final Map<String, Object> inputItemDataMap;

    public InputMap() {
        inputTransitionNameMap = new HashMap<>();
        inputItemDataMap = new HashMap<>();
    }

    public void addMapping(String input, Object transitionName, Object itemData) {
        inputTransitionNameMap.put(input, transitionName);
        inputItemDataMap.put(input, itemData);
    }

    public Optional<Object> getItemDataForInput(String input) {
        return Optional.ofNullable(inputItemDataMap.get(input));
    }

    public boolean hasTransitionNameForInput(Object transitionName, String input) {
        return transitionName.equals(inputTransitionNameMap.get(input));
    }

    public Optional<Object> getTransitionName(String input) {
        return Optional.ofNullable(inputTransitionNameMap.get(input));
    }
}
