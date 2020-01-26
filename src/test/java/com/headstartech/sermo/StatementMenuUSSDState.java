package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class StatementMenuUSSDState implements USSDState {

    @Override
    public String getId() {
        return "STATEMENT";
    }

    @Override
    public String onEntry(USSDSupport ussdSupport) {
        return "statement";
    }

    @Override
    public void onExit(USSDSupport ussdSupport, Object event) {
    }

    @Override
    public String onInternal(USSDSupport ussdSupport, Object event) {
        return null;
    }
}
