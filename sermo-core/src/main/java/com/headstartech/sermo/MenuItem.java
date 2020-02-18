package com.headstartech.sermo;

import java.util.Objects;

/**
 * @author Per Johansson
 */
public class MenuItem {

    private final String label;
    private final Object transition;
    private final Object itemData;

    public MenuItem(String label, Object transition, Object itemData) {
        Objects.requireNonNull(label, "label must be non-null");
        Objects.requireNonNull(transition, "transition must be non-null");
        this.transition = transition;
        this.label = label;
        this.itemData = itemData;
    }

    public MenuItem(String label, Object transition) {
        this(label, transition, null);
    }

    public Object getTransition() {
        return transition;
    }

    public String getLabel() {
        return label;
    }

    public Object getItemData() {
        return itemData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return label.equals(menuItem.label) &&
                transition.equals(menuItem.transition) &&
                Objects.equals(itemData, menuItem.itemData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, transition, itemData);
    }
}
