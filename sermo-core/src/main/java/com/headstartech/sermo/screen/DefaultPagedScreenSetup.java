package com.headstartech.sermo.screen;

import java.util.ArrayList;
import java.util.List;

public class DefaultPagedScreenSetup implements PagedScreenSetup {

    private final List<ScreenBlock> pages = new ArrayList<>();
    private final NextPageMenuItem nextPageMenuItem;
    private final PreviousPageMenuItem previousPageMenuItem;
    private final ScreenBlock headerBlock;
    private final ScreenBlock footerBlock;
    private final ScreenBlock firstPageHeaderBlock;
    private final ScreenBlock firstPageFooterBlock;
    private int page;

    public DefaultPagedScreenSetup(List<ScreenBlock> pages, NextPageMenuItem nextPageMenuItem, PreviousPageMenuItem previousPageMenuItem, ScreenBlock headerBlock, ScreenBlock footerBlock, ScreenBlock firstPageHeaderBlock, ScreenBlock firstPageFooterBlock) {
        this.pages.addAll(pages);
        this.nextPageMenuItem = nextPageMenuItem;
        this.previousPageMenuItem = previousPageMenuItem;
        this.headerBlock = headerBlock;
        this.footerBlock = footerBlock;
        this.firstPageHeaderBlock = firstPageHeaderBlock;
        this.firstPageFooterBlock = firstPageFooterBlock;
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
        if(page == 0 && firstPageHeaderBlock != null) {
            return firstPageHeaderBlock;
        } else {
            return headerBlock;
        }
    }

    @Override
    public ScreenBlock getFooterBlock() {
        if(page == 0 && firstPageFooterBlock != null) {
            return firstPageFooterBlock;
        } else {
            return footerBlock;
        }
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
        clampPage();
    }

    @Override
    public void decrementPage() {
        page--;
        clampPage();
    }

    private void clampPage() {
        if(pages.isEmpty()) {
            page = 0;
        } else {
            page = clamp(0, pages.size() - 1, page);
        }
    }

    private int clamp(int min, int max, int value) {
        return Math.min(Math.max(value, min), max);
    }

    public static DefaultPagedScreenSetup.Builder builder() {
        return new DefaultPagedScreenSetup.Builder();
    }

    public static class Builder {
        private List<ScreenBlock> pages = new ArrayList<>();

        private NextPageMenuItem nextPageMenuItem;
        private PreviousPageMenuItem previousPageMenuItem;

        private ScreenBlock headerBlock;
        private ScreenBlock footerBlock;

        private ScreenBlock firstPageHeaderBlock;
        private ScreenBlock firstPageFooterBlock;

        public Builder withPages(List<ScreenBlock> pages) {
            this.pages.addAll(pages);
            return this;
        }

        public Builder withNextPageMenuItem(NextPageMenuItem nextPageMenuItem) {
            this.nextPageMenuItem = nextPageMenuItem;
            return this;
        }

        public Builder withPreviousPageMenuItem(PreviousPageMenuItem previousPageMenuItem) {
            this.previousPageMenuItem = previousPageMenuItem;
            return this;
        }

        public Builder withHeaderBlock(ScreenBlock headerBlock) {
            this.headerBlock = headerBlock;
            return this;
        }

        public Builder withFirstPageHeaderBlock(ScreenBlock firstPageHeaderBlock) {
            this.firstPageHeaderBlock = firstPageHeaderBlock;
            return this;
        }

        public Builder withFooterBlock(ScreenBlock footerBlock) {
            this.footerBlock = footerBlock;
            return this;
        }

        public Builder withFirstPageFooterBlock(ScreenBlock firstPageFooterBlock) {
            this.firstPageFooterBlock = firstPageFooterBlock;
            return this;
        }

        public DefaultPagedScreenSetup build() {
            return new DefaultPagedScreenSetup(pages, nextPageMenuItem, previousPageMenuItem, headerBlock, footerBlock, firstPageHeaderBlock, firstPageFooterBlock);
        }

    }
}
