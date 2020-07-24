package com.headstartech.sermo.statemachine.actions;

import org.springframework.statemachine.ExtendedState;

@FunctionalInterface
public interface OnItemHandler {

    void handle(ExtendedState extendedState);
}
