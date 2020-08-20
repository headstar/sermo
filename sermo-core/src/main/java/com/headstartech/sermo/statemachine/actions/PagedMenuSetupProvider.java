package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.PagedMenuSetup;
import org.springframework.statemachine.StateContext;

public interface PagedMenuSetupProvider<S, E extends DialogEvent> {

    PagedMenuSetup getPagedScreenSetup(StateContext<S, E> context);
}
