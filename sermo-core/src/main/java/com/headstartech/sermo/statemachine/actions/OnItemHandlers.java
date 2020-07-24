package com.headstartech.sermo.statemachine.actions;

import org.springframework.statemachine.ExtendedState;

public class OnItemHandlers {

    private OnItemHandlers() {}

    public static Object setVariable(Object key, Object value) {
        return new SetVariableItemHandler(key, value);
    }

    private static class SetVariableItemHandler implements OnItemHandler {
        private final Object key;
        private final Object value;

        public SetVariableItemHandler(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void handle(ExtendedState extendedState) {
            extendedState.getVariables().put(key, value);
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("SetVariableItemHandler{");
            sb.append("key=").append(key);
            sb.append(", value=").append(value);
            sb.append('}');
            return sb.toString();
        }
    }
}
