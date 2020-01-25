package com.headstartech.sermo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Per Johansson
 */
public class AccountsMenu extends PagedScreenUSSDState {

    public AccountsMenu() {
        super(2);
    }

    @Override
    public String getId() {
        return "BALANCE";
    }

    @Override
    protected List<MenuItem> getAllItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Account A", RootMenuItems.ACCOUNT_DETAIL, "a"));
        items.add(new MenuItem("Account B", RootMenuItems.ACCOUNT_DETAIL, "b"));
        items.add(new MenuItem("Account C", RootMenuItems.ACCOUNT_DETAIL, "c"));
        items.add(new MenuItem("Account D", RootMenuItems.ACCOUNT_DETAIL, "d"));
        items.add(new MenuItem("Account E", RootMenuItems.ACCOUNT_DETAIL, "e"));
        return items;
    }



    @Override
    protected NextPageMenuItem getNextScreenItem() {
        return new NextPageMenuItem("0", "Next page");
    }

    @Override
    protected PreviousPageMenuItem getPreviousScreenItem() {
        return new PreviousPageMenuItem("#", "Previous page");
    }
}
