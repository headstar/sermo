/*
 *  Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.headstartech.sermo.screen;

import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.ExtendedState;

import java.util.List;

/**
 * @author Per Johansson
 */
public class DefaultPagedMenuSupport implements PagedMenuSupport {

    private final ScreenRenderer screenRenderer;

    public DefaultPagedMenuSupport(ScreenRenderer screenRenderer) {
        this.screenRenderer = screenRenderer;
    }

    public DefaultPagedMenuSupport() {
        this(new DefaultScreenRenderer());
    }

    @Override
    public void initializePagedScreen(ExtendedState extendedState, PagedMenuSetup pagedMenuSetup) {
        ExtendedStateSupport.setPagedMenuSetup(extendedState, pagedMenuSetup);
    }

    @Override
    public Screen createScreen(ExtendedState extendedState) {
        PagedMenuSetup pagedMenuSetup = ExtendedStateSupport.getPagedMenuSetup(extendedState);

        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenRenderer(screenRenderer);
        screenBuilder.withScreenBlock(pagedMenuSetup.getHeaderBlock());

        List<MenuItem> allMenuItems = pagedMenuSetup.getAllMenuItems();
        int page = pagedMenuSetup.getPage();
        screenBuilder.withScreenBlock(getMenuGroupForCurrentPage(allMenuItems, page, pagedMenuSetup.getPageSize(), pagedMenuSetup.getMenuItemElide()));

        if(hasNextPage(allMenuItems.size(), page, pagedMenuSetup.getPageSize())) {
            screenBuilder.withScreenBlock(pagedMenuSetup.getNextPageMenuItem());
        }
        if(page > 0) {
            screenBuilder.withScreenBlock(pagedMenuSetup.getPreviousPageMenuItem());
        }

        screenBuilder.withScreenBlock(pagedMenuSetup.getFooterBlock());
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

    private MenuGroup getMenuGroupForCurrentPage(List<MenuItem> allItems, int pageIndex, int pageSize, TextElide elide) {
        MenuGroup.Builder builder = MenuGroup.builder();
        builder.withElide(elide);

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
        PagedMenuSetup pagedMenuSetup = ExtendedStateSupport.getPagedMenuSetup(extendedState);
        adjustPage(pagedMenuSetup, 1);
    }

    private void decrementPageIndex(ExtendedState extendedState) {
        PagedMenuSetup pagedMenuSetup = ExtendedStateSupport.getPagedMenuSetup(extendedState);
        adjustPage(pagedMenuSetup, -1);
    }

    private void adjustPage(PagedMenuSetup pagedMenuSetup, int diff) {
        int page = pagedMenuSetup.getPage() + diff;
        page = page < 0 ? 0 : page;
        pagedMenuSetup.setPage(page);

    }

    private int getNumberOfPages(int numItems, int pageSize) {
        return (numItems / pageSize) + ((numItems % pageSize) > 0 ? 1 : 0);
    }
}
