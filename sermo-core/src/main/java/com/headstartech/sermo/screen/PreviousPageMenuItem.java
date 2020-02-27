package com.headstartech.sermo.screen;

import com.headstartech.sermo.USSDSystemConstants;

/**
 * @author Per Johansson
 */
public class PreviousPageMenuItem extends StaticMenuItem {

    public PreviousPageMenuItem(String input, String label) {
        super(input, label, USSDSystemConstants.PREVIOUS_PAGE_KEY);
    }
}
