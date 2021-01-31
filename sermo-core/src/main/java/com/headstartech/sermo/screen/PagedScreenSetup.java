package com.headstartech.sermo.screen;

public interface PagedScreenSetup {

    public ScreenBlock getPageBody();

    NextPageMenuItem getNextPageMenuItem();

    PreviousPageMenuItem getPreviousPageMenuItem();

    ScreenBlock getHeaderBlock();

    ScreenBlock getFooterBlock();

    void incrementPage();

    void decrementPage();

    boolean hasNextPage();

    boolean hasPreviousPage();

}
