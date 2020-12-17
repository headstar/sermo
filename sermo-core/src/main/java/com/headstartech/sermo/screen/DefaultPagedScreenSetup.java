package com.headstartech.sermo.screen;

import java.util.List;

public class DefaultPagedScreenSetup implements PagedScreenSetup {

    private final List<ScreenBlock> pages;
    private final NextPageMenuItem nextPageMenuItem;
    private final PreviousPageMenuItem previousPageMenuItem;
    private final ScreenBlock headerBlock;
    private final ScreenBlock footerBlock;
    private int page;

    public DefaultPagedScreenSetup(List<ScreenBlock> pages, NextPageMenuItem nextPageMenuItem, PreviousPageMenuItem previousPageMenuItem, ScreenBlock headerBlock, ScreenBlock footerBlock) {
        this.pages = pages;
        this.nextPageMenuItem = nextPageMenuItem;
        this.previousPageMenuItem = previousPageMenuItem;
        this.headerBlock = headerBlock;
        this.footerBlock = footerBlock;
    }

    @Override
    public ScreenBlock getScreenBlockForCurrentPage() {
        if(pages.isEmpty()) {
            return null;
        }
        return pages.get(page);
    }

    @Override
    public NextPageMenuItem getNextPageMenuItem() {
        return nextPageMenuItem;
    }

    @Override
    public PreviousPageMenuItem getPreviousPageMenuItem() {
        return previousPageMenuItem;
    }

    @Override
    public ScreenBlock getHeaderBlock() {
        return headerBlock;
    }

    @Override
    public ScreenBlock getFooterBlock() {
        return footerBlock;
    }

    @Override
    public boolean hasNextPage() {
        return page < (pages.size() - 1);
    }

    @Override
    public boolean hasPreviousPage() {
        return page > 0;
    }

    @Override
    public void incrementPage() {
        page++;
        page = Math.min(pages.size() - 1, page);
    }

    @Override
    public void decrementPage() {
        page--;
        page = Math.max(0, page);
    }
}
