package com.headstartech.sermo;

import java.util.List;
import java.util.Optional;

/**
 * @author Per Johansson
 */
public abstract class PagedScreenUSSDState extends MenuUSSDState {

    private final String PAGE_INDEX_CONTEXT_KEY = "PageIndex";
    private final String ALL_ITEMS_CONTEXT_KEY = "AllItems";

    private final int pageSize;

    public PagedScreenUSSDState(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public final String onEntry(USSDSupport ussdSupport) {
        List<MenuItem> allItems = getAllItems();
        addItemsToContext(ussdSupport, allItems);

        Screen screen = createScreen(ussdSupport);
        setMenu(ussdSupport, screen);
        return screen.getOutput();
    }

    @Override
    public String onInternal(USSDSupport ussdSupport, Object event) {
        Optional<Object> transitionKeyOpt = ussdSupport.getTransitionKey(event);
        if(transitionKeyOpt.isPresent()) {
            Object transitionKey = transitionKeyOpt.get();
            if(transitionKey.equals(ExtendedStateKeys.NEXT_PAGE_KEY)) {
                incrementPageIndex(ussdSupport);
            } else if(transitionKey.equals(ExtendedStateKeys.PREVIOUS_PAGE_KEY)) {
                decrementtPageIndex(ussdSupport);
            }
        }
        Screen screen = createScreen(ussdSupport);
        setMenu(ussdSupport, screen);
        return screen.getOutput();
    }

    protected abstract List<MenuItem> getAllItems();

    protected abstract NextPageMenuItem getNextScreenItem();

    protected abstract PreviousPageMenuItem getPreviousScreenItem();

    protected ScreenBlock getHeaderBlock() {
        return null;
    }

    protected ScreenBlock getFooterBlock() {
        return null;
    }

    protected Screen createScreen(USSDSupport ussdSupport) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(getHeaderBlock());

        int pageIndex = getPageIndex(ussdSupport);
        screenBuilder.withScreenBlock(getMenuGroupForCurrentPage(getItemsFromContext(ussdSupport), pageIndex));

        if(hasNextPage(ussdSupport, pageIndex)) {
            screenBuilder.withScreenBlock(getNextScreenItem());
        }
        if(pageIndex > 0) {
            screenBuilder.withScreenBlock(getPreviousScreenItem());
        }

        screenBuilder.withScreenBlock(getFooterBlock());
        return screenBuilder.build();
    }

    private MenuGroup getMenuGroupForCurrentPage(List<MenuItem> allItems, int pageIndex) {
        MenuGroup.Builder builder = MenuGroup.builder();
        int startIndex = pageSize * pageIndex;
        for(int i=startIndex; i<startIndex + pageSize; ++i) {
            if(i < allItems.size()) {
                builder.withMenuItem(allItems.get(i));
            }
        }
        return builder.build();
    }

    private boolean hasNextPage(USSDSupport ussdSupport, int pageIndex) {
        int numItems = getNumItems(ussdSupport);
        int numPages = getNumberOfPages(numItems);
        int maxPageIndex = numPages - 1;
        return pageIndex < maxPageIndex;
    }

    private void addItemsToContext(USSDSupport ussdSupport, List<MenuItem> menuItems) {
        ussdSupport.getVariables().put(getAllItemsContextKey(), menuItems);
    }

    private int getNumItems(USSDSupport ussdSupport) {
        return getItemsFromContext(ussdSupport).size();
    }

    @SuppressWarnings("unchecked")
    private List<MenuItem> getItemsFromContext(USSDSupport ussdSupport) {
        return (List<MenuItem>) ussdSupport.getVariables().get(getAllItemsContextKey());
    }

    private int getPageIndex(USSDSupport ussdSupport) {
        Integer pageIndex = (Integer) ussdSupport.getVariables().get(getPageIndexContextKey());
        return pageIndex != null ? pageIndex : 0;
    }

    private void setPageIndex(USSDSupport ussdSupport, int pageIndex) {
        ussdSupport.getVariables().put(getPageIndexContextKey(), pageIndex);
    }

    private void incrementPageIndex(USSDSupport ussdSupport) {
        int pageIndex = getPageIndex(ussdSupport);
        setPageIndex(ussdSupport, pageIndex + 1);
    }

    private void decrementtPageIndex(USSDSupport ussdSupport) {
        int pageIndex = getPageIndex(ussdSupport);
        setPageIndex(ussdSupport, pageIndex - 1);
    }


    private String getPageIndexContextKey() {
        return String.format("%s_%s", getId(), PAGE_INDEX_CONTEXT_KEY);
    }

    private String getAllItemsContextKey() {
        return String.format("%s_%s", getId(), ALL_ITEMS_CONTEXT_KEY);
    }

    private int getNumberOfPages(int numItems) {
        return (numItems / pageSize) + ((numItems % pageSize) > 0 ? 1 : 0);
    }


}
