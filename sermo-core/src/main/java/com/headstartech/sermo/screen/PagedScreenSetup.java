package com.headstartech.sermo.screen;

public interface PagedScreenSetup {

    public ScreenBlock getScreenBlockForCurrentPage();

    NextPageMenuItem getNextPageMenuItem();

    PreviousPageMenuItem getPreviousPageMenuItem();

    ScreenBlock getHeaderBlock();

    ScreenBlock getFooterBlock();

    void incrementPage();

    void decrementPage();

    boolean hasNextPage();

    boolean hasPreviousPage();

}
