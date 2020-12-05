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

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.DefaultPagedScreenSupport;
import com.headstartech.sermo.screen.PagedScreenSupport;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class DefaultPagedScreenEntryAction<S, E extends DialogEvent> implements Action<S, E> {

    private final PagedScreenSetupProvider<S, E> pagedScreenSetupProvider;
    private final PagedScreenSupport pagedScreenSupport;

    public DefaultPagedScreenEntryAction(PagedScreenSetupProvider<S, E> pagedScreenSetupProvider, PagedScreenSupport pagedScreenSupport) {
        this.pagedScreenSetupProvider = pagedScreenSetupProvider;
        this.pagedScreenSupport = pagedScreenSupport;
    }

    public DefaultPagedScreenEntryAction(PagedScreenSetupProvider<S, E> pagedScreenSetupProvider) {
        this.pagedScreenSetupProvider = pagedScreenSetupProvider;
        this.pagedScreenSupport = new DefaultPagedScreenSupport();
    }

    @Override
    public void execute(StateContext<S, E> context) {
        pagedScreenSupport.initializePagedScreen(context.getExtendedState(), pagedScreenSetupProvider.getPagedScreenSetup(context));
        Screen screen = pagedScreenSupport.createScreen(context.getExtendedState());

        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }

}
