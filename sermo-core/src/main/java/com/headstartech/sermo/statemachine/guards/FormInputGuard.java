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

package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.function.Predicate;

/**
 * {@link Guard} based on a {@link Predicate}.
 *
 * Used to accept/reject user input.
 *
 * @author Per Johansson
 */
public class FormInputGuard<S, E extends DialogEvent> extends GuardBase<S, E> {

    private static final Logger log = LoggerFactory.getLogger(FormInputGuard.class);

    private final Predicate<String> predicate;

    public FormInputGuard(Predicate<String> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean result = predicate.test(input);
        log.debug("Form input guard evaluation: result={}, input={}, predicate={}", result, input, predicate);
        return result;
    }


}
