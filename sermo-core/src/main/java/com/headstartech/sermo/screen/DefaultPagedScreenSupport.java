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
    public void cleanupPagedScreen(ExtendedState extendedState) {
        ExtendedStateSupport.removePagedScreenSetup(extendedState);
    }

    @Override
    public Screen createScreen(ExtendedState extendedState) {
        PagedScreenSetup pagedScreenSetup = ExtendedStateSupport.getPagedScreenSetup(extendedState);

        Screen.Builder screenBuilder =  Screen.builder();
        screenBuilder.withScreenRenderer(screenRenderer);

        addHeader(screenBuilder, pagedScreenSetup);
        addBody(screenBuilder, pagedScreenSetup);
        addNextAndPreviousPage(screenBuilder, pagedScreenSetup);
        addFooter(screenBuilder, pagedScreenSetup);

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

    protected void addHeader(Screen.Builder builder, PagedScreenSetup pagedScreenSetup) {
        pagedScreenSetup.getHeaderBlock().ifPresent(e -> builder.withScreenBlock(e));
    }

    protected void addBody(Screen.Builder builder, PagedScreenSetup pagedScreenSetup) {
        pagedScreenSetup.getPageBody().ifPresent(e -> builder.withScreenBlock(e));
    }

    protected void addNextAndPreviousPage(Screen.Builder builder, PagedScreenSetup pagedScreenSetup) {
        if(pagedScreenSetup.hasNextPage()) {
            pagedScreenSetup.getNextPageMenuItem().ifPresent(e -> builder.withScreenBlock(e));
        }
        if(pagedScreenSetup.hasPreviousPage()) {
            pagedScreenSetup.getPreviousPageMenuItem().ifPresent(e -> builder.withScreenBlock(e));
        }
    }

    protected void addFooter(Screen.Builder builder, PagedScreenSetup pagedScreenSetup) {
        pagedScreenSetup.getFooterBlock().ifPresent(e -> builder.withScreenBlock(e));
    }
}
