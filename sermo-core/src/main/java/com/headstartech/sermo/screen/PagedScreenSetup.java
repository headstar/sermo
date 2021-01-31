package com.headstartech.sermo.screen;

import java.util.Optional;

public interface PagedScreenSetup {

    Optional<ScreenBlock> getPageBody();

    Optional<NextPageMenuItem> getNextPageMenuItem();

    Optional<PreviousPageMenuItem> getPreviousPageMenuItem();

    Optional<ScreenBlock> getHeaderBlock();

    Optional<ScreenBlock> getFooterBlock();

    void incrementPage();

    void decrementPage();

    boolean hasNextPage();

    boolean hasPreviousPage();

}
