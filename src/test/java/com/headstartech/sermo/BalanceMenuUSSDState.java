package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class BalanceMenuUSSDState extends MenuUSSDState {

    @Override
    public String getId() {
        return "BALANCE";
    }

    @Override
    public String onEntry(USSDSupport ussdSupport) {
        Menu menu =  Menu.builder()
                .withMenuItem("Account 1", RootMenuItems.ACCOUNT_DETAIL, "account1")
                .withMenuItem("Account 2", RootMenuItems.ACCOUNT_DETAIL, "account2")
                .withMenuItem("Account 3", RootMenuItems.ACCOUNT_DETAIL, "account3")
                .build();

        setMenu(ussdSupport, menu);
        return menu.render();
    }
}
