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
        Menu menu =  Menu.builder()
                .withMenuItem("Balance", RootMenuItems.BALANCE)
                .withMenuItem("Statement", RootMenuItems.STATEMENT)
                .build();

        setMenu(ussdSupport, menu);
        return menu.render();
    }

    @Override
    public String onEvent(USSDSupport ussdSupport, Object event) {
        return null;
    }

}
