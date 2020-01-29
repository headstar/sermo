package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;

import java.util.Map;
import java.util.Optional;

/**
 * @author Per Johansson
 */
public class DefaultUSSDSupport implements USSDSupport {

    private final ExtendedState extendedState;

    public DefaultUSSDSupport(ExtendedState extendedState) {
        this.extendedState = extendedState;
    }

    @Override
    public Map<Object, Object> getVariables() {
        return extendedState.getVariables();
    }

    @Override
    public Optional<Object> getItemKey() {
        return Optional.ofNullable(extendedState.getVariables().get(ExtendedStateKeys.INPUT_ITEM_KEY));
    }

    @Override
    public Optional<Object> getTransition() {
        return Optional.ofNullable(extendedState.getVariables().get(ExtendedStateKeys.TRANSITION_KEY));
    }

}
