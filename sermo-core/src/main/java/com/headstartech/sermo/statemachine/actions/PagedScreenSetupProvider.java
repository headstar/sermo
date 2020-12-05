package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.PagedScreenSetup;
import org.springframework.statemachine.StateContext;

public interface PagedScreenSetupProvider<S, E extends DialogEvent> {

    PagedScreenSetup getPagedScreenSetup(StateContext<S, E> context);
}
