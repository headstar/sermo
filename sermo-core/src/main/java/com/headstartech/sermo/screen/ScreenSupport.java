package com.headstartech.sermo.screen;

import org.springframework.statemachine.ExtendedState;

/**
 * @author Per Johansson
 */
public interface ScreenSupport {

    void initializePagedScreen(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup);

    Screen createScreen(ExtendedState extendedState);

    void incrementPage(ExtendedState extendedState);

    void decrementPage(ExtendedState extendedState);
}
