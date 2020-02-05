package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class MenuItem {

    private final String label;
    private final Object transition;
    private final Object itemData;

    public MenuItem(String label, Object transition, Object itemData) {
        this.transition = transition;
        this.label = label;
        this.itemData = itemData;
    }

    public MenuItem(String label, Object transition) {
        this.label = label;
        this.transition = transition;
        this.itemData = null;
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
}
