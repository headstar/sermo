package com.headstartech.sermo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Per Johansson
 */
public class MenuGroup implements ScreenBlock {

    private final List<MenuItem> menuItems;

    public MenuGroup(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<MenuItem> menuItems = new ArrayList<>();

        public Builder withMenuItem(MenuItem menuItem) {
            menuItems.add(menuItem);
            return this;
        }

        public Builder withMenuItem(String label, Object transitionKey) {
            menuItems.add(new MenuItem(label, transitionKey));
            return this;
        }

        public Builder withMenuItem(String label, Object transitionKey, Object itemKey) {
            menuItems.add(new MenuItem(label, transitionKey, itemKey));
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(menuItems);
        }
    }

}
