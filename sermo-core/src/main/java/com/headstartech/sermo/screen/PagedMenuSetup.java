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

import java.util.List;

/**
 * @author Per Johansson
 */
public class PagedMenuSetup {

    private final List<MenuItem> allMenuItems;
    private final NextPageMenuItem nextPageMenuItem;
    private final PreviousPageMenuItem previousPageMenuItem;
    private final ScreenBlock headerBlock;
    private final ScreenBlock footerBlock;
    private final int pageSize;
    private int page;
    private final TextElide menuItemElide;

    public PagedMenuSetup(List<MenuItem> allMenuItems, TextElide menuItemElide, NextPageMenuItem nextPageMenuItem, PreviousPageMenuItem previousPageMenuItem, ScreenBlock headerBlock, ScreenBlock footerBlock, int pageSize) {
        this.allMenuItems = allMenuItems;
        this.menuItemElide = menuItemElide;
        this.nextPageMenuItem = nextPageMenuItem;
        this.previousPageMenuItem = previousPageMenuItem;
        this.headerBlock = headerBlock;
        this.footerBlock = footerBlock;
        this.pageSize = pageSize;
    }

    public PagedMenuSetup(List<MenuItem> allMenuItems, NextPageMenuItem nextPageMenuItem, PreviousPageMenuItem previousPageMenuItem, ScreenBlock headerBlock, ScreenBlock footerBlock, int pageSize) {
        this(allMenuItems, new TextElide(), nextPageMenuItem, previousPageMenuItem, headerBlock, footerBlock, pageSize);
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

    public TextElide getMenuItemElide() {
        return menuItemElide;
    }
}
