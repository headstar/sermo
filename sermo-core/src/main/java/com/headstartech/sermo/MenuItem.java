package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class MenuItem {

    private final String label;
    private final Object transitionName;
    private final Object itemKey;

    public MenuItem(String label, Object transitionName, Object itemKey) {
        this.transitionName = transitionName;
        this.label = label;
        this.itemKey = itemKey;
    }

    public MenuItem(String label, Object transitionName) {
        this.label = label;
        this.transitionName = transitionName;
        this.itemKey = null;
    }



    public Object getTransitionName() {
        return transitionName;
    }

    public String getLabel() {
        return label;
    }

    public Object getItemKey() {
        return itemKey;
    }
}
