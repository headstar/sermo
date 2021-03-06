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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Per Johansson
 */
public class RegExpTransitionGuard<S, E extends DialogEvent> extends GuardBase<S, E> {

    private static final Logger log = LoggerFactory.getLogger(RegExpTransitionGuard.class);

    private final Pattern pattern;

    public RegExpTransitionGuard(Pattern pattern) {
        this.pattern = pattern;
    }

    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        Matcher m = pattern.matcher(input);
        boolean res = m.matches();
        log.debug("Regexp transition guard evaluation: result={}, input={}, pattern={}", res, input, pattern.pattern());
        return res;
    }

}
