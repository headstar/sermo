package com.headstartech.sermo.screen;

import com.headstartech.sermo.USSDSystemConstants;

/**
 * @author Per Johansson
 */
public class NextPageMenuItem extends StaticMenuItem {

    public NextPageMenuItem(String input, String label) {
        super(input, label, USSDSystemConstants.NEXT_PAGE_KEY);
    }
}
