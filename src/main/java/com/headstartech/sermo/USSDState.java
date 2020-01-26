package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public interface USSDState {

    String getId();

    String onEntry(USSDSupport ussdSupport);

    String onInternal(USSDSupport ussdSupport, Object event);

    void onExit(USSDSupport ussdSupport, Object event);

}
