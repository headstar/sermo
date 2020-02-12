package com.headstartech.sermo;

import com.headstartech.sermo.MOInput;

/**
 * @author Per Johansson
 */
public class SubscriberEvent extends MOInput {

    private final String msisdn;

    public SubscriberEvent() {
        msisdn = null;
    }

    public SubscriberEvent(String input, String msisdn) {
        super(input);
        this.msisdn = msisdn;
    }
}
