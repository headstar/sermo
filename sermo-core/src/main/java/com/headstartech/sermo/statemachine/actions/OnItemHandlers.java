package com.headstartech.sermo.statemachine.actions;

import org.springframework.statemachine.ExtendedState;

import java.util.function.Consumer;

public class OnItemHandlers {

    private OnItemHandlers() {}

    public static Object setExtendedStateVariable(Object key, Object value) {
        return new SetVariableItemHandler(key, value);
    }

    public static <T> Object modifyExtendedStateVariable(Object key, Class<T> clazz, Consumer<T> consumer) {
        return new ModifyingVariableItemHandler<>(key, clazz, consumer);
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

    // TODO: Consumer not serializable
    private static class ModifyingVariableItemHandler <T> implements OnItemHandler {
        private final Object key;
        private final Class<T> clazz;
        private final Consumer<T> consumer;

        public ModifyingVariableItemHandler(Object key, Class<T> clazz, Consumer<T> consumer) {
            this.key = key;
            this.clazz = clazz;
            this.consumer = consumer;
        }

        @Override
        public void handle(ExtendedState extendedState) {
            consumer.accept(extendedState.get(key, clazz));
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("ModifyingVariableItemHandler{");
            sb.append("key=").append(key);
            sb.append(", clazz=").append(clazz);
            sb.append(", consumer=").append(consumer);
            sb.append('}');
            return sb.toString();
        }
    }
}
