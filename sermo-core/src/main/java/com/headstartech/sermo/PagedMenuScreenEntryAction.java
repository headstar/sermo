package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public abstract class PagedMenuScreenEntryAction<S, E> extends MenuScreenEntryAction<S, E> {

    private final ScreenSupport screenSupport;

    public PagedMenuScreenEntryAction(ScreenSupport screenSupport) {
        this.screenSupport = screenSupport;
    }

    public PagedMenuScreenEntryAction() {
        this(new DefaultScreenSupport());
    }

    @Override
    public void execute(StateContext<S, E> context) {
        screenSupport.initializePagedScreen(context.getExtendedState(), getPagedScreenSetup(context));
        Screen screen = screenSupport.createScreen(context.getExtendedState());

        ExtendedStateSupport.setScreenMenuInputMap(context.getExtendedState(), screen.getInputMap());
        ExtendedStateSupport.setOutput(context.getExtendedState(), screen.getOutput());
    }

    protected abstract PagedScreenSetup getPagedScreenSetup(StateContext<S, E> context);

}
