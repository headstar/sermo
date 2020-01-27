package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class MenuItem {

    private final String label;
    private final Object transitionKey;
    private final Object itemKey;

    public MenuItem(String label, Object transitionKey, Object itemKey) {
        this.transitionKey = transitionKey;
        this.label = label;
        this.itemKey = itemKey;
    }

    public MenuItem(String label, Object transitionKey) {
        this.label = label;
        this.transitionKey = transitionKey;
        this.itemKey = null;
    }



    public Object getTransitionKey() {
        return transitionKey;
    }

    public String getLabel() {
        return label;
    }

    public Object getItemKey() {
        return itemKey;
    }
}
