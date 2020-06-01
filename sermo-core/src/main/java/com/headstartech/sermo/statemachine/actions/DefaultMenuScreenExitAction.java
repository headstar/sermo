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
 * Action:
 *   * fetching item data (if any) associated with given input and setting in {@link ExtendedState}.
 *   * clearing {@link InputMap} from {@link ExtendedState}.
 *
 * @author Per Johansson
 */
public class DefaultMenuScreenExitAction<S, E extends DialogEvent> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {
        transferItemData(context.getExtendedState(), context.getEvent());
        clearScreenInputMap(context.getExtendedState());
    }

    protected void transferItemData(ExtendedState extendedState, DialogEvent event) {
        InputMap inputMap = ExtendedStateSupport.getScreenMenuInputMap(extendedState);
        if (inputMap != null) {
            Optional<Object> itemData = inputMap.getItemDataForInput(event.getInput());
            if(itemData.isPresent()) {
                ExtendedStateSupport.setItemData(extendedState, itemData.get());
            }
        }
    }

    protected void clearScreenInputMap(ExtendedState extendedState) {
        ExtendedStateSupport.clearScreenMenuInputMap(extendedState);
    }
}