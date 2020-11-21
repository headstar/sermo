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

import com.headstartech.sermo.support.ExtendedStateSupport;
import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.InputMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;

/**
 * A {@link org.springframework.statemachine.guard.Guard} returning true/false based on whether the user input has been associated with a transition or not.
 *
 * @see {@link com.headstartech.sermo.screen.MenuItem}
 * @see {@link com.headstartech.sermo.screen.Screen}
 *
 * @author Per Johansson
 */
public class ScreenTransitionGuard<S, E extends DialogEvent> extends GuardBase<S, E> {

    private static final Logger log = LoggerFactory.getLogger(ScreenTransitionGuard.class);

    private final Object transitionId;

    public ScreenTransitionGuard(Object transitionId) {
        this.transitionId = transitionId;
    }

    public Object getTransitionId() {
        return transitionId;
    }

    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean res = false;
        InputMap inputMap = ExtendedStateSupport.getScreenMenuInputMap(context.getExtendedState());
        if(inputMap != null && inputMap.hasTransitionForInput(transitionId, input)) {
            res = true;
        }
        log.debug("Screen transition guard evaluation: result={}, input={}, transitionId={}", res, input, transitionId);
        return res;
    }
}
