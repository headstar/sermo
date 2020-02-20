package com.headstartech.sermo.screen;

import com.headstartech.sermo.ExtendedStateKeys;
import com.headstartech.sermo.screen.StaticMenuItem;

/**
 * @author Per Johansson
 */
public class PreviousPageMenuItem extends StaticMenuItem {

    public PreviousPageMenuItem(String input, String label) {
        super(input, label, ExtendedStateKeys.PREVIOUS_PAGE_KEY);
    }
}
