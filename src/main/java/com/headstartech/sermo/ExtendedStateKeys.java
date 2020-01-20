package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;

import java.util.Map;

/**
 * @author Per Johansson
 */
public class ExtendedStateKeys {

    public static final Object INPUT_TRANSITION_MAP = new Object();
    public static final Object INPUT_ITEM_MAP = new Object();
    public static final Object INPUT_ITEM_KEY = new Object();
    public static final Object SUPPORT_KEY = new Object();
    public static final Object OUTPUT_KEY = new Object();

    @SuppressWarnings("unchecked")
    static Map<Object, Object> getInputTransitionKeyMap(ExtendedState extendedState) {
        return (Map<Object, Object>)extendedState.get(INPUT_TRANSITION_MAP, Map.class);
    }
}
