package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.DefaultPagedScreenSupport;
import com.headstartech.sermo.screen.PagedScreenSupport;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;

public class PagedScreenInternalAction<S, E extends DialogEvent> extends AbstractPagedScreenInternalAction<S, E> {

    private final PagedScreenSupport pagedScreenSupport;

    public PagedScreenInternalAction(PagedScreenSupport pagedScreenSupport) {
        this.pagedScreenSupport = pagedScreenSupport;
    }

    public PagedScreenInternalAction() {
        this(new DefaultPagedScreenSupport());
    }

    @Override
    protected void handleNextPage(StateContext<S, E> context) {
        pagedScreenSupport.incrementPage(context.getExtendedState());
        createScreen(context);
    }

    @Override
    protected void handlePreviousPage(StateContext<S, E> context) {
        pagedScreenSupport.decrementPage(context.getExtendedState());
        createScreen(context);
    }

    protected void createScreen(StateContext<S, E> context) {
        Screen screen = pagedScreenSupport.createScreen(context.getExtendedState());
        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }
}
