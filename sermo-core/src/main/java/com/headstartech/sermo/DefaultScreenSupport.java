package com.headstartech.sermo;

import org.springframework.statemachine.ExtendedState;

import java.util.List;

/**
 * @author Per Johansson
 */
public class DefaultScreenSupport implements ScreenSupport {

    @Override
    public void initializePagedScreen(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup) {
        ExtendedStateSupport.setPagedScreenSetup(extendedState, pagedScreenSetup);
    }

    @Override
    public Screen createScreen(ExtendedState extendedState) {
        // TODO: check for null and throw!!
        PagedScreenSetup pagedScreenSetup = ExtendedStateSupport.getPagedScreenSetup(extendedState);

        Screen.Builder screenBuilder =  Screen.builder();
        screenBuilder.withScreenBlock(pagedScreenSetup.getHeaderBlock());

        List<MenuItem> allMenuItems = pagedScreenSetup.getAllMenuItems();
        int page = pagedScreenSetup.getPage();
        screenBuilder.withScreenBlock(getMenuGroupForCurrentPage(allMenuItems, page, pagedScreenSetup.getPageSize()));

        if(hasNextPage(allMenuItems.size(), page, pagedScreenSetup.getPageSize())) {
            screenBuilder.withScreenBlock(pagedScreenSetup.getNextPageMenuItem());
        }
        if(page > 0) {
            screenBuilder.withScreenBlock(pagedScreenSetup.getPreviousPageMenuItem());
        }

        screenBuilder.withScreenBlock(pagedScreenSetup.getFooterBlock());
        return screenBuilder.build();
    }

    @Override
    public void incrementPage(ExtendedState extendedState) {
        incrementPageIndex(extendedState);
    }

    @Override
    public void decrementPage(ExtendedState extendedState) {
        decrementPageIndex(extendedState);
    }

    private MenuGroup getMenuGroupForCurrentPage(List<MenuItem> allItems, int pageIndex, int pageSize) {
        MenuGroup.Builder builder = MenuGroup.builder();
        int startIndex = pageSize * pageIndex;
        for(int i=startIndex; i<startIndex + pageSize; ++i) {
            if(i < allItems.size()) {
                builder.withMenuItem(allItems.get(i));
            }
        }
        return builder.build();
    }

    private boolean hasNextPage(int totalItemCount, int page, int pageSize) {
        int numPages = getNumberOfPages(totalItemCount, pageSize);
        int maxPage = numPages - 1;
        return page < maxPage;
    }


    private void incrementPageIndex(ExtendedState extendedState) {
        PagedScreenSetup pagedScreenSetup = ExtendedStateSupport.getPagedScreenSetup(extendedState);
        adjustPage(pagedScreenSetup, 1);
    }

    private void decrementPageIndex(ExtendedState extendedState) {
        PagedScreenSetup pagedScreenSetup = ExtendedStateSupport.getPagedScreenSetup(extendedState);
        adjustPage(pagedScreenSetup, -1);
    }

    // TODO: check page limits (page < 0 etc...)
    private void adjustPage(PagedScreenSetup pagedScreenSetup, int diff) {
        int page = pagedScreenSetup.getPage() + diff;
        pagedScreenSetup.setPage(page);

    }

    private int getNumberOfPages(int numItems, int pageSize) {
        return (numItems / pageSize) + ((numItems % pageSize) > 0 ? 1 : 0);
    }
}