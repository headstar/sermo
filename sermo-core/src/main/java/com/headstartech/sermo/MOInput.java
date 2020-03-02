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

import org.springframework.statemachine.trigger.TriggerContext;

/**
 * @author Per Johansson
 */
public class MOInput {

    public static final MOInput INSTANCE = new MOInput();

    protected final String input;

    public MOInput() {
        input = null;
    }

    public MOInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public final int hashCode() {
        return 17;
    }

    /**
     * Sending any event extending this class to the state machine will result in {@link org.springframework.statemachine.trigger.EventTrigger#evaluate(TriggerContext)} returning true
     * and configured {@link org.springframework.statemachine.guard.Guard}s will control if any transition will be executed.
     *
     */
    @Override
    public final boolean equals(Object o) {
        return MOInput.class.isAssignableFrom(o.getClass());
    }

    @Override
    public String toString() {
        return "MOInput [input=" + input + "]";
    }
}
