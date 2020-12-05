package com.headstartech.sermo.screen;

import org.springframework.statemachine.ExtendedState;

public interface PagedScreenSupport {

    void initializePagedScreen(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup);

    Screen createScreen(ExtendedState extendedState);

    void incrementPage(ExtendedState extendedState);

    void decrementPage(ExtendedState extendedState);

}
