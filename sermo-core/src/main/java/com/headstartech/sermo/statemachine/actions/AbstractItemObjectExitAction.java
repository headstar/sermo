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
import com.headstartech.sermo.screen.InputMap;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Optional;

/**
 *
 * @author Per Johansson
 */
public abstract class AbstractItemObjectExitAction<S, E extends DialogEvent> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {
        transferItemData(context.getExtendedState(), context.getEvent());
        clearScreenInputMap(context.getExtendedState());
    }

    protected void transferItemData(ExtendedState extendedState, DialogEvent event) {
        InputMap inputMap = ExtendedStateSupport.getScreenMenuInputMap(extendedState);
        if (inputMap != null) {
            Optional<Object> itemObject = inputMap.getItemDataForInput(event.getInput());
            if(itemObject.isPresent()) {
                handleItemObject(extendedState, itemObject.get());
            }
        }
    }

    protected abstract void handleItemObject(ExtendedState extendedState, Object itemObject);

    protected void clearScreenInputMap(ExtendedState extendedState) {
        ExtendedStateSupport.clearScreenMenuInputMap(extendedState);
    }
}
