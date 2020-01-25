package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class Page {

    private final MenuGroup menuGroup;
    private final boolean hasNextPage;

    public Page(MenuGroup menuGroup, boolean hasNextPage) {
        this.menuGroup = menuGroup;
        this.hasNextPage = hasNextPage;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }
}
