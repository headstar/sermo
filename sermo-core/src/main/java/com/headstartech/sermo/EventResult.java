package com.headstartech.sermo;

import java.util.Optional;
import java.util.StringJoiner;

/**
 * @author Per Johansson
 */
public class EventResult {

    private final String output;
    private final boolean applicationError;
    private final boolean applicationCompleted;

    private EventResult(String output, boolean applicationError, boolean applicationCompleted) {
        this.output = output;
        this.applicationError = applicationError;
        this.applicationCompleted = applicationCompleted;
    }

    public Optional<String> getOutput() {
        return Optional.ofNullable(output);
    }

    public boolean isApplicationError() {
        return applicationError;
    }

    public boolean isApplicationCompleted() {
        return applicationCompleted;
    }

    static EventResult ofOutput(String output) {
        return new EventResult(output, false, false);
    }

    static EventResult ofApplicationCompleted(String output) {
        return new EventResult(output, false, true);
    }

    static EventResult ofApplicationError() {
        return new EventResult(null, true, false);
    }

    static EventResult ofApplicationError(String output) {
        return new EventResult(output, true, false);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EventResult.class.getSimpleName() + "[", "]")
                .add("output='" + output + "'")
                .add("applicationError=" + applicationError)
                .add("applicationCompleted=" + applicationCompleted)
                .toString();
    }
}
