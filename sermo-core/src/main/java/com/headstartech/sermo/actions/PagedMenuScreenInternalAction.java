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

package com.headstartech.sermo.actions;

import com.headstartech.sermo.DefaultPagedScreenSupport;
import com.headstartech.sermo.ExtendedStateSupport;
import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.USSDSystemConstants;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.PagedScreenSupport;
import org.springframework.statemachine.StateContext;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public class PagedMenuScreenInternalAction<S, E extends MOInput> extends MenuScreenEntryAction<S, E> {

    private final PagedScreenSupport pagedScreenSupport;

    public PagedMenuScreenInternalAction(PagedScreenSupport pagedScreenSupport) {
        this.pagedScreenSupport = pagedScreenSupport;
    }

    public PagedMenuScreenInternalAction() {
        this(new DefaultPagedScreenSupport());
    }

    @Override
    public void execute(StateContext<S, E> context) {

        Optional<Object> transitionNameOpt = ExtendedStateSupport.getTransition(context.getExtendedState(), (context.getEvent()).getInput());
        if(transitionNameOpt.get().equals(USSDSystemConstants.NEXT_PAGE_KEY)) {
            pagedScreenSupport.incrementPage(context.getExtendedState());
        } else if(transitionNameOpt.get().equals(USSDSystemConstants.PREVIOUS_PAGE_KEY)) {
            pagedScreenSupport.decrementPage(context.getExtendedState());
        } else {
            throw new IllegalStateException("Should never happen!");
        }

        Screen screen = pagedScreenSupport.createScreen(context.getExtendedState());
        setScreen(context.getExtendedState(), screen);
    }

}
