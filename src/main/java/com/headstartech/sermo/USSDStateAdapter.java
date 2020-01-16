package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public abstract class USSDStateAdapter implements USSDState {

    @Override
    public void onExit(USSDSupport ussdSupport) {

    }

    @Override
    public String onEvent(USSDSupport ussdSupport, Object event) {
        return null;
    }
}
