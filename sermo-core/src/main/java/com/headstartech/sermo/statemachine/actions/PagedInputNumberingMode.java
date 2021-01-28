package com.headstartech.sermo.statemachine.actions;

/**
 * Defines enumeration of how input items should be numbered in a paged menu.
 *
 */
public enum PagedInputNumberingMode {

    /**
     * First item on each screen always starts with 1.
     */
    STARTING_AT_ONE,

    /**
     * First item on each screen is given the number it has in the overall ordering.
     * E.g. with 4 items on each screen the first item on the second screen will be number 5.
     */
    CONTINUOUS
}
