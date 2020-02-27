package com.headstartech.sermo.actions;

import com.headstartech.sermo.DefaultScreenSupport;
import com.headstartech.sermo.USSDSystemConstants;
import com.headstartech.sermo.ExtendedStateSupport;
import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.ScreenSupport;
import org.springframework.statemachine.StateContext;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class PagedMenuScreenInternalAction<S, E extends MOInput> extends MenuScreenEntryAction<S, E> {

    private final ScreenSupport screenSupport;

    public PagedMenuScreenInternalAction(ScreenSupport screenSupport) {
        this.screenSupport = screenSupport;
    }

    public PagedMenuScreenInternalAction() {
        this(new DefaultScreenSupport());
    }

    @Override
    public void execute(StateContext<S, E> context) {

        Optional<Object> transitionNameOpt = ExtendedStateSupport.getTransition(context.getExtendedState(), (context.getEvent()).getInput());
        if(transitionNameOpt.get().equals(USSDSystemConstants.NEXT_PAGE_KEY)) {
            screenSupport.incrementPage(context.getExtendedState());
        } else if(transitionNameOpt.get().equals(USSDSystemConstants.PREVIOUS_PAGE_KEY)) {
            screenSupport.decrementPage(context.getExtendedState());
        } else {
            throw new IllegalStateException("Should never happen!");
        }

        Screen screen = screenSupport.createScreen(context.getExtendedState());
        ExtendedStateSupport.setScreenMenuInputMap(context.getExtendedState(), screen.getInputMap());
        ExtendedStateSupport.setOutput(context.getExtendedState(), screen.getOutput());
    }

}
