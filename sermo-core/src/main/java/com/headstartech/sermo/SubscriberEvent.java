package com.headstartech.sermo;

import com.headstartech.sermo.MOInput;

import java.util.StringJoiner;

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

    public String getMsisdn() {
        return msisdn;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SubscriberEvent.class.getSimpleName() + "[", "]")
                .add("msisdn='" + msisdn + "'")
                .add("input='" + input + "'")
                .toString();
    }
}
