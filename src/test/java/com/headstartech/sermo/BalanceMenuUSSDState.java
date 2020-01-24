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
        Screen.Builder screenBuilder =  Screen.builder();

        MenuGroup accounts = MenuGroup.builder()
                .withMenuItem("Account 1", RootMenuItems.ACCOUNT_DETAIL, "account1")
                .withMenuItem("Account 2", RootMenuItems.ACCOUNT_DETAIL, "account2")
                .withMenuItem("Account 3", RootMenuItems.ACCOUNT_DETAIL, "account3")
                .build();

        screenBuilder.withScreenBlock(new Text("Your account"));
        screenBuilder.withScreenBlock(accounts);

        Screen screen = screenBuilder.build();
        setMenu(ussdSupport, screen);
        return screen.getOutput();
    }
}
