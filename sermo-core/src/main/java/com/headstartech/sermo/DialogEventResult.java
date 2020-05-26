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
 * Result from {@link SermoDialogExecutor}.
 *
 * @author Per Johansson
 */
public class DialogEventResult {

    private final String output;
    private final boolean dialogComplete;

    public DialogEventResult(String output, boolean dialogComplete) {
        this.output = output;
        this.dialogComplete = dialogComplete;
    }

    /**
     * Resulting output after handling input.
     *
     * @return
     */
    public Optional<String> getOutput() {
        return Optional.ofNullable(output);
    }

    /**
     * Incicates if the dialog is complete. When the dialog is complete no more input is accepted.
     *
     * @return
     */
    public boolean isDialogComplete() {
        return dialogComplete;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DialogEventResult.class.getSimpleName() + "[", "]")
                .add("output='" + output + "'")
                .add("dialogComplete=" + dialogComplete)
                .toString();
    }

}
