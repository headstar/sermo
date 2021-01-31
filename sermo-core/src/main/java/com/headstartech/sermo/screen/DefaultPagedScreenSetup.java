package com.headstartech.sermo.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultPagedScreenSetup implements PagedScreenSetup {

    private final List<ScreenBlock> pageBodies;
    private final NextPageMenuItem nextPageMenuItem;
    private final PreviousPageMenuItem previousPageMenuItem;
    private final ScreenBlock headerBlock;
    private final ScreenBlock footerBlock;
    private final ScreenBlock firstPageHeaderBlock;
    private final ScreenBlock firstPageFooterBlock;
    private int page = 0;

    public DefaultPagedScreenSetup(List<ScreenBlock> pageBodies, NextPageMenuItem nextPageMenuItem, PreviousPageMenuItem previousPageMenuItem, ScreenBlock headerBlock, ScreenBlock footerBlock, ScreenBlock firstPageHeaderBlock, ScreenBlock firstPageFooterBlock) {
        this.pageBodies = pageBodies;
        this.nextPageMenuItem = nextPageMenuItem;
        this.previousPageMenuItem = previousPageMenuItem;
        this.headerBlock = headerBlock;
        this.footerBlock = footerBlock;
        this.firstPageHeaderBlock = firstPageHeaderBlock;
        this.firstPageFooterBlock = firstPageFooterBlock;
    }

    @Override
    public Optional<ScreenBlock> getPageBody() {
        if(pageBodies.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(pageBodies.get(page));
    }

    @Override
    public Optional<NextPageMenuItem> getNextPageMenuItem() {
        return Optional.ofNullable(nextPageMenuItem);
    }

    @Override
    public Optional<PreviousPageMenuItem> getPreviousPageMenuItem() {
        return Optional.ofNullable(previousPageMenuItem);
    }

    @Override
    public Optional<ScreenBlock> getHeaderBlock() {
        if(page == 0 && firstPageHeaderBlock != null) {
            return Optional.of(firstPageHeaderBlock);
        } else {
            return Optional.ofNullable(headerBlock);
        }
    }

    @Override
    public Optional<ScreenBlock> getFooterBlock() {
        if(page == 0 && firstPageFooterBlock != null) {
            return Optional.of(firstPageFooterBlock);
        } else {
            return Optional.ofNullable(footerBlock);
        }
    }

    @Override
    public boolean hasNextPage() {
        return page < (pageBodies.size() - 1);
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
        if(pageBodies.isEmpty()) {
            page = 0;
        } else {
            page = clamp(0, pageBodies.size() - 1, page);
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
