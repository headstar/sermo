package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;

import java.util.Map;
import java.util.Optional;

import static com.headstartech.sermo.ExtendedStateKeys.INPUT_ITEM_KEY;

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
    public Optional<Object> getTransitionKey(Object event) {
        Optional<Object> res = Optional.empty();
        Map<String, Object> inputTransitionMap = (Map<String, Object>) getVariables().get(ExtendedStateKeys.INPUT_TRANSITION_MAP);
        if (inputTransitionMap != null) {
            if (event instanceof MOInput) {
                MOInput moInput = (MOInput) event;
                res = Optional.ofNullable(inputTransitionMap.get(moInput.getInput()));
            }
        }
        return res;
    }
}
