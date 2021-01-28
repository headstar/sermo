package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.screen.MenuGroup;
import com.headstartech.sermo.screen.MenuItem;
import com.headstartech.sermo.screen.ScreenBlock;
import com.headstartech.sermo.screen.TextElide;

import java.util.ArrayList;
import java.util.List;

/**
 * Utiility methods to create a list of {@link MenuGroup}s based on a list of {@link MenuItem}s suitable for paging.
 *
 */
public class PagedMenuItemsUtil {

    private PagedMenuItemsUtil() {}

    public static List<ScreenBlock> getScreenBlockForMenuItems(List<MenuItem> allItems, int pageSize, TextElide elide, PagedInputNumberingMode pagedInputNumberingMode) {
        int numberOfPages = getNumberOfPages(allItems.size(), pageSize);

        List<ScreenBlock> screenBlocks = new ArrayList<>();
        for(int page=0; page<numberOfPages; ++page) {
            screenBlocks.add(getMenuGroupForPage(allItems, page, pageSize, elide, pagedInputNumberingMode));
        }
        return screenBlocks;

    }

    public static List<ScreenBlock> getScreenBlockForMenuItems(List<MenuItem> allItems, int pageSize, TextElide elide) {
        return getScreenBlockForMenuItems(allItems, pageSize, elide, PagedInputNumberingMode.CONTINUOUS);
    }

    private static MenuGroup getMenuGroupForPage(List<MenuItem> allItems, int page, int pageSize, TextElide elide, PagedInputNumberingMode pagedInputNumberingMode) {
        MenuGroup.Builder builder = MenuGroup.builder();
        builder.withElide(elide);

        int startIndex = pageSize * page;
        for(int i=startIndex; i<startIndex + pageSize; ++i) {
            if(i < allItems.size()) {
                builder.withMenuItem(allItems.get(i));
            }
        }

        builder.withInputNumberingStartAt(getInputNumberingStart(startIndex, pagedInputNumberingMode));
        return builder.build();
    }

    private static int getInputNumberingStart(int startIndex, PagedInputNumberingMode pagedInputNumberingMode) {
        if(PagedInputNumberingMode.STARTING_AT_ONE.equals(pagedInputNumberingMode)) {
            return 1;
        } else if(PagedInputNumberingMode.CONTINUOUS.equals(pagedInputNumberingMode)) {
            return startIndex + 1;
        } else {
            throw new RuntimeException(String.format("Unknown PagedInputNumberingMode: %s", pagedInputNumberingMode));
        }
    }

    private static int getNumberOfPages(int numItems, int pageSize) {
        return (numItems / pageSize) + ((numItems % pageSize) > 0 ? 1 : 0);
    }
}
