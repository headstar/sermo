package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public abstract class USSDStateAdapter implements USSDState {

    @Override
    public String onEntry(USSDSupport ussdSupport) {
        return null;
    }

    @Override
    public String onInternal(USSDSupport ussdSupport, Object event) {
        return null;
    }

    @Override
    public void onExit(USSDSupport ussdSupport, Object event) {

    }

}
