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
import com.headstartech.sermo.SermoSystemConstants;
import com.headstartech.sermo.screen.InputMap;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Optional;

import static com.headstartech.sermo.SermoSystemConstants.INPUT_ITEM_DATA_KEY;

/**
 * @author Per Johansson
 */
public class MenuScreenExitAction<S, E extends DialogEvent> implements Action<S, E> {

    @Override
    public void execute(StateContext<S, E> context) {
        transferItemKey(context.getExtendedState(), context.getEvent());
        clearScreen(context.getExtendedState());
    }

    protected void clearScreen(ExtendedState extendedState) {
        extendedState.getVariables().remove(SermoSystemConstants.INPUT_MAP_KEY);
    }

    protected void transferItemKey(ExtendedState extendedState, DialogEvent event) {
        InputMap inputMap = (InputMap) extendedState.getVariables().get(SermoSystemConstants.INPUT_MAP_KEY);
        if (inputMap != null) {
            Optional<Object> itemData = inputMap.getItemDataForInput(event.getInput());
            if(itemData.isPresent()) {
                extendedState.getVariables().put(INPUT_ITEM_DATA_KEY, itemData.get());
            }
        }
    }
}
