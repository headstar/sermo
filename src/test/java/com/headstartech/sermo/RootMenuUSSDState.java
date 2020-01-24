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

        MenuGroup accounts = MenuGroup.builder()
                .withMenuItem("Account 1", RootMenuItems.BALANCE)
                .withMenuItem("Account 2", RootMenuItems.STATEMENT)
                .build();

        screenBuilder.withScreenBlock(new Text("Main Menu"));
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
