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

package com.headstartech.sermo;

import com.headstartech.sermo.screen.*;
import org.springframework.statemachine.ExtendedState;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Per Johansson
 */
public class DefaultPagedScreenSupport implements PagedScreenSupport {

    private final Supplier<ScreenRenderer> screenRendererSupplier;

    public DefaultPagedScreenSupport(Supplier<ScreenRenderer> screenRendererSupplier) {
        this.screenRendererSupplier = screenRendererSupplier;
    }

    public DefaultPagedScreenSupport() {
        this( () -> new DefaultScreenRenderer());
    }

    @Override
    public void initializePagedScreen(ExtendedState extendedState, PagedScreenSetup pagedScreenSetup) {
        ExtendedStateSupport.setPagedScreenSetup(extendedState, pagedScreenSetup);
    }

    @Override
    public Screen createScreen(ExtendedState extendedState) {
        PagedScreenSetup pagedScreenSetup = ExtendedStateSupport.getPagedScreenSetup(extendedState);

        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenRenderer(screenRendererSupplier.get());
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

    private void adjustPage(PagedScreenSetup pagedScreenSetup, int diff) {
        int page = pagedScreenSetup.getPage() + diff;
        page = page < 0 ? 0 : page;
        pagedScreenSetup.setPage(page);

    }

    private int getNumberOfPages(int numItems, int pageSize) {
        return (numItems / pageSize) + ((numItems % pageSize) > 0 ? 1 : 0);
    }
}
