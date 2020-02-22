package com.headstartech.sermo.screen;

import java.util.Objects;

/**
 * @author Per Johansson
 */
public class MenuItem {

    private final String label;
    private final Object transitionId;
    private final Object itemData;

    public MenuItem(String label, Object transitionId, Object itemData) {
        Objects.requireNonNull(label, "label must be non-null");
        Objects.requireNonNull(transitionId, "transitionId must be non-null");
        this.transitionId = transitionId;
        this.label = label;
        this.itemData = itemData;
    }

    public MenuItem(String label, Object transitionId) {
        this(label, transitionId, null);
    }

    public Object getTransition() {
        return transitionId;
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
                transitionId.equals(menuItem.transitionId) &&
                Objects.equals(itemData, menuItem.itemData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, transitionId, itemData);
    }
}
