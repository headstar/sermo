package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.screen.MenuGroup;
import com.headstartech.sermo.screen.MenuItem;
import com.headstartech.sermo.screen.ScreenBlock;
import com.headstartech.sermo.screen.TextElide;

import java.util.ArrayList;
import java.util.List;

public class PagedMenuItemsUtil {

    private PagedMenuItemsUtil() {}

    public static List<ScreenBlock> getScreenBlockForMenuItems(List<MenuItem> allItems, int pageSize, TextElide elide) {
        int numberOfPages = getNumberOfPages(allItems.size(), pageSize);

        List<ScreenBlock> screenBlocks = new ArrayList<>();
        for(int page=0; page<numberOfPages; ++page) {
            screenBlocks.add(getMenuGroupForPage(allItems, page, pageSize, elide));
        }
        return screenBlocks;
    }

    private static MenuGroup getMenuGroupForPage(List<MenuItem> allItems, int page, int pageSize, TextElide elide) {
        MenuGroup.Builder builder = MenuGroup.builder();
        builder.withElide(elide);

        int startIndex = pageSize * page;
        for(int i=startIndex; i<startIndex + pageSize; ++i) {
            if(i < allItems.size()) {
                builder.withMenuItem(allItems.get(i));
            }
        }
        return builder.build();
    }

    private static int getNumberOfPages(int numItems, int pageSize) {
        return (numItems / pageSize) + ((numItems % pageSize) > 0 ? 1 : 0);
    }
}
