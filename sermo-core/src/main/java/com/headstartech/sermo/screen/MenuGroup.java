package com.headstartech.sermo.screen;

import java.util.*;

/**
 * @author Per Johansson
 */
public class MenuGroup implements ScreenBlock {

    private final List<MenuItem> menuItems;

    public MenuGroup(List<MenuItem> menuItems) {
        Objects.requireNonNull(menuItems, "menuItems must be non-null");
        this.menuItems = Collections.unmodifiableList(menuItems);
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public void accept(ScreenBlockVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return menuItems.equals(menuGroup.menuItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuItems);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenuGroup.class.getSimpleName() + "[", "]")
                .add("menuItems=" + menuItems)
                .toString();
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
