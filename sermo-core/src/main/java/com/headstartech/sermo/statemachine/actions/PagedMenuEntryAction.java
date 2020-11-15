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
import com.headstartech.sermo.screen.DefaultPagedMenuSupport;
import com.headstartech.sermo.screen.PagedMenuSupport;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class PagedMenuEntryAction<S, E extends DialogEvent> implements Action<S, E> {

    private final PagedMenuSetupProvider<S, E> pagedMenuSetupProvider;
    private final PagedMenuSupport pagedMenuSupport;

    public PagedMenuEntryAction(PagedMenuSetupProvider<S, E> pagedMenuSetupProvider, PagedMenuSupport pagedMenuSupport) {
        this.pagedMenuSetupProvider = pagedMenuSetupProvider;
        this.pagedMenuSupport = pagedMenuSupport;
    }

    public PagedMenuEntryAction(PagedMenuSetupProvider<S, E> pagedMenuSetupProvider) {
        this.pagedMenuSetupProvider = pagedMenuSetupProvider;
        this.pagedMenuSupport = new DefaultPagedMenuSupport();
    }

    @Override
    public void execute(StateContext<S, E> context) {
        pagedMenuSupport.initializePagedScreen(context.getExtendedState(), pagedMenuSetupProvider.getPagedScreenSetup(context));
        Screen screen = pagedMenuSupport.createScreen(context.getExtendedState());

        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }

}