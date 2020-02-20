package com.headstartech.sermo.screen;

import java.util.List;

/**
 * @author Per Johansson
 */
public class PagedScreenSetup {

    private final List<MenuItem> allMenuItems;
    private final NextPageMenuItem nextPageMenuItem;
    private final PreviousPageMenuItem previousPageMenuItem;
    private final ScreenBlock headerBlock;
    private final ScreenBlock footerBlock;
    private final int pageSize;
    private int page;


    public PagedScreenSetup(List<MenuItem> allMenuItems, NextPageMenuItem nextPageMenuItem, PreviousPageMenuItem previousPageMenuItem, ScreenBlock headerBlock, ScreenBlock footerBlock, int pageSize) {
        this.allMenuItems = allMenuItems;
        this.nextPageMenuItem = nextPageMenuItem;
        this.previousPageMenuItem = previousPageMenuItem;
        this.headerBlock = headerBlock;
        this.footerBlock = footerBlock;
        this.pageSize = pageSize;
    }

    public List<MenuItem> getAllMenuItems() {
        return allMenuItems;
    }

    public NextPageMenuItem getNextPageMenuItem() {
        return nextPageMenuItem;
    }

    public PreviousPageMenuItem getPreviousPageMenuItem() {
        return previousPageMenuItem;
    }

    public ScreenBlock getHeaderBlock() {
        return headerBlock;
    }

    public ScreenBlock getFooterBlock() {
        return footerBlock;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
