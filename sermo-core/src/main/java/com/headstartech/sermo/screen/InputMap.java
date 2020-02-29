package com.headstartech.sermo.screen;

import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputMap inputMap = (InputMap) o;
        return inputTransitionMap.equals(inputMap.inputTransitionMap) &&
                inputItemDataMap.equals(inputMap.inputItemDataMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputTransitionMap, inputItemDataMap);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InputMap.class.getSimpleName() + "[", "]")
                .add("inputTransitionMap=" + inputTransitionMap)
                .add("inputItemDataMap=" + inputItemDataMap)
                .toString();
    }
}
