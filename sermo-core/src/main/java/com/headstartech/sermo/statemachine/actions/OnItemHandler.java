package com.headstartech.sermo.statemachine.actions;

import org.springframework.statemachine.ExtendedState;

/**
 * Interface to handle selected items in menus, typically transferring the object associated with the selected menu item.
 *
 * See {@link OnItemHandlers} for convenience implementations.
 *
 */
@FunctionalInterface
public interface OnItemHandler {

    void handle(ExtendedState extendedState);
}
