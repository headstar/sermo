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

import com.headstartech.sermo.statemachine.guards.ScreenTransitionGuard;
import org.springframework.statemachine.guard.Guard;

/**
 * Base class for events handled by {@link SermoDialogExecutor}.
 *
 * Note, the equals() method always returns {@code true}. This to let {@link Guard} implementations control if transitions should be executed or not.
 *
 * @see ScreenTransitionGuard
 *
 * @author Per Johansson
 */
public class DialogEvent {
    
    protected final String input;

    public DialogEvent() {
        input = null;
    }

    public DialogEvent(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public final int hashCode() {
        return 17;
    }

    @Override
    public final boolean equals(Object o) {
        return true;
    }

    @Override
    public String toString() {
        return "DiaglogEvent [input=" + input + "]";
    }
}
