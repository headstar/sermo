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

package com.headstartech.sermo.states;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class XPagedUSSDState<S, E extends DialogEvent> extends DefaultUSSDState<S, E> implements IPagedUSSDState<S, E> {

    private final Action<S, E> internalAction;

    public XPagedUSSDState(S id, Action<S, E> entryAction, Action<S, E> exitAction, Action<S, E> internalAction) {
        super(id, entryAction, exitAction, false);
        this.internalAction = internalAction;
    }

    @Override
    public Action<S, E> toNextOrToPreviousPageAction() {
        return internalAction;
    }
}
