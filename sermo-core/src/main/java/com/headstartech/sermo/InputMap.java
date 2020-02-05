package com.headstartech.sermo;

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

    public void addMapping(String input, Object transition, Object itemData) {
        inputTransitionMap.put(input, transition);
        inputItemDataMap.put(input, itemData);
    }

    public Optional<Object> getItemDataForInput(String input) {
        return Optional.ofNullable(inputItemDataMap.get(input));
    }

    public boolean hasTransitionForInput(Object transition, String input) {
        return transition.equals(inputTransitionMap.get(input));
    }

    public Optional<Object> getTransition(String input) {
        return Optional.ofNullable(inputTransitionMap.get(input));
    }
}
