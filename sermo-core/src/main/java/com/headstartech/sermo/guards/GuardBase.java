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

package com.headstartech.sermo.guards;


import com.headstartech.sermo.MOInput;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

/**
 * @author Per Johansson
 */
public abstract class GuardBase<S, E extends MOInput> implements Guard<S, E> {

    @Override
    public boolean evaluate(StateContext<S, E> context) {
        if(context.getEvent() != null && context.getEvent().getInput() != null) {
            return doEvaluate(context,  context.getEvent().getInput());
        }
        return false;
    }

    protected abstract boolean doEvaluate(StateContext<S, E> context, String input);
}
