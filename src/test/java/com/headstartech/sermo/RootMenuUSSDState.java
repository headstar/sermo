package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class RootMenuUSSDState extends MenuUSSDState implements USSDState {

    @Override
    public String getId() {
        return "ROOT";
    }

    @Override
    public String onEntry(USSDSupport ussdSupport) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Main Menu"));

        MenuGroup accounts = MenuGroup.builder()
                .withMenuItem("Accounts", RootMenuItems.ACCOUNTS)
                .withMenuItem("Statements", RootMenuItems.STATEMENT)
                .build();
        screenBuilder.withScreenBlock(accounts);

        Screen screen = screenBuilder.build();
        setMenu(ussdSupport, screen);
        return screen.getOutput();
    }

    @Override
    public String onEvent(USSDSupport ussdSupport, Object event) {
        return null;
    }

}
