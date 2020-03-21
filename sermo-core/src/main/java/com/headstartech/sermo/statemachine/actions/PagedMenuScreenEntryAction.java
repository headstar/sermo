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

package com.headstartech.sermo.statemachine.actions;

import com.headstartech.sermo.screen.DefaultPagedScreenSupport;
import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.PagedScreenSetup;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.PagedScreenSupport;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public abstract class PagedMenuScreenEntryAction<S, E extends DialogEvent> extends MenuScreenEntryAction<S, E> {

    private final PagedScreenSupport pagedScreenSupport;

    public PagedMenuScreenEntryAction(PagedScreenSupport pagedScreenSupport) {
        this.pagedScreenSupport = pagedScreenSupport;
    }

    public PagedMenuScreenEntryAction() {
        this(new DefaultPagedScreenSupport());
    }

    @Override
    public void execute(StateContext<S, E> context) {
        pagedScreenSupport.initializePagedScreen(context.getExtendedState(), getPagedScreenSetup(context));
        Screen screen = pagedScreenSupport.createScreen(context.getExtendedState());

        setScreen(context.getExtendedState(), screen);
    }

    protected abstract PagedScreenSetup getPagedScreenSetup(StateContext<S, E> context);

}
