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

package demo.web.dialog;

import com.headstartech.sermo.screen.EmptyLine;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.StaticMenuItem;
import com.headstartech.sermo.screen.Text;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class InterestOfferEntryAction implements Action<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        Screen.Builder screenBuilder =  Screen.builder();

         Long age = Long.valueOf(context.getEvent().getInput());

        screenBuilder.withScreenBlock(new Text(String.format("At age %d your interest offer is 3%%!", age)));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);
        screenBuilder.withScreenBlock(new StaticMenuItem("#", "Main menu", Transitions.ROOT));

        Screen screen = screenBuilder.build();

        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }
}
