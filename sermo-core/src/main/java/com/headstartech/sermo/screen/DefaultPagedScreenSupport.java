package com.headstartech.sermo.screen;

import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.ExtendedState;

public class DefaultPagedScreenSupport implements PagedScreenSupport {

    private final ScreenRenderer screenRenderer;

    public DefaultPagedScreenSupport(ScreenRenderer screenRenderer) {
        this.screenRenderer = screenRenderer;
    }

    public DefaultPagedScreenSupport() {
        this(new DefaultScreenRenderer());
    }

    @Override
    public void initializePagedScreen(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup) {
        ExtendedStateSupport.setPagedScreenSetup(extendedState, pagedScreenSetup);
    }

    @Override
    public Screen createScreen(ExtendedState extendedState) {
        PagedScreenSetup pagedScreenSetup = ExtendedStateSupport.getPagedScreenSetup(extendedState);

        Screen.Builder screenBuilder =  Screen.builder();
        screenBuilder.withScreenRenderer(screenRenderer);

        screenBuilder.withScreenBlock(pagedScreenSetup.getHeaderBlock());

        screenBuilder.withScreenBlock(pagedScreenSetup.getScreenBlockForCurrentPage());

        if(pagedScreenSetup.hasNextPage()) {
            screenBuilder.withScreenBlock(pagedScreenSetup.getNextPageMenuItem());
        }
        if(pagedScreenSetup.hasPreviousPage()) {
            screenBuilder.withScreenBlock(pagedScreenSetup.getPreviousPageMenuItem());
        }

        screenBuilder.withScreenBlock(pagedScreenSetup.getFooterBlock());

        return screenBuilder.build();
    }

    @Override
    public void incrementPage(ExtendedState extendedState) {
        PagedScreenSetup setup = ExtendedStateSupport.getPagedScreenSetup(extendedState);
        setup.incrementPage();
    }

    @Override
    public void decrementPage(ExtendedState extendedState) {
        PagedScreenSetup setup = ExtendedStateSupport.getPagedScreenSetup(extendedState);
        setup.decrementPage();
    }

}
