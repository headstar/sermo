package com.headstartech.sermo;

import java.util.Optional;
import java.util.StringJoiner;

/**
 * @author Per Johansson
 */
public class EventResult {

    private final String output;
    private final ApplicationState applicationState;

    private EventResult(String output, ApplicationState applicationState) {
        this.output = output;
        this.applicationState = applicationState;
    }

    public Optional<String> getOutput() {
        return Optional.ofNullable(output);
    }

    public ApplicationState getApplicationState() {
        return applicationState;
    }

    static EventResult ofOutput(String output) {
        return new EventResult(output, ApplicationState.IN_PROGRESS);
    }

    static EventResult ofApplicationCompleted(String output) {
        return new EventResult(output, ApplicationState.COMPLETE);
    }

    static EventResult ofApplicationError(String output) {
        return new EventResult(output, ApplicationState.ERROR);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", EventResult.class.getSimpleName() + "[", "]")
                .add("output='" + output + "'")
                .add("applicationState=" + applicationState)
                .toString();
    }

    public enum ApplicationState {
        IN_PROGRESS, COMPLETE, ERROR
    }
}
