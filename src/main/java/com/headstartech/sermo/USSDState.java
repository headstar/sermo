package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public interface USSDState {

    String getId();

    String onEntry(USSDSupport ussdSupport);

    void onExit(USSDSupport ussdSupport, Object event);

    String onEvent(USSDSupport ussdSupport, Object event);
}
