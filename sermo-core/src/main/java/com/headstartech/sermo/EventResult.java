/*
 *  Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
