package com.headstartech.sermo.actions;

import com.headstartech.sermo.DefaultScreenSupport;
import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.screen.PagedScreenSetup;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.ScreenSupport;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public abstract class PagedMenuScreenEntryAction<S, E extends MOInput> extends MenuScreenEntryAction<S, E> {

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

        setScreen(context.getExtendedState(), screen);
    }

    protected abstract PagedScreenSetup getPagedScreenSetup(StateContext<S, E> context);

}
