package com.headstartech.sermo.screen;

import com.headstartech.sermo.ExtendedStateKeys;
import com.headstartech.sermo.screen.StaticMenuItem;

/**
 * @author Per Johansson
 */
public class NextPageMenuItem extends StaticMenuItem {

    public NextPageMenuItem(String input, String label) {
        super(input, label, ExtendedStateKeys.NEXT_PAGE_KEY);
    }
}
