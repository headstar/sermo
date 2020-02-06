package com.headstartech.sermo;

import org.springframework.statemachine.StateContext;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class PagedMenuScreenInternalAction<S, E> extends MenuScreenEntryAction<S, E> {

    private final ScreenSupport screenSupport;

    public PagedMenuScreenInternalAction(ScreenSupport screenSupport) {
        this.screenSupport = screenSupport;
    }

    public PagedMenuScreenInternalAction() {
        this(new DefaultScreenSupport());
    }

    @Override
    public void execute(StateContext<S, E> context) {

        if(context.getEvent() instanceof MOInput) {
            Optional<Object> transitionNameOpt = ExtendedStateSupport.getTransition(context.getExtendedState(), ((MOInput) context.getEvent()).getInput());
            if(transitionNameOpt.get().equals(ExtendedStateKeys.NEXT_PAGE_KEY)) {
                screenSupport.incrementPage(context.getExtendedState());
            } else if(transitionNameOpt.get().equals(ExtendedStateKeys.PREVIOUS_PAGE_KEY)) {
                screenSupport.decrementPage(context.getExtendedState());
            } else {
                throw new IllegalStateException("Should never happen!");
            }

            Screen screen = screenSupport.createScreen(context.getExtendedState());
            ExtendedStateSupport.setScreenMenuInputMap(context.getExtendedState(), screen.getInputMap());
            ExtendedStateSupport.setOutput(context.getExtendedState(), screen.getOutput());
        }
    }

}
