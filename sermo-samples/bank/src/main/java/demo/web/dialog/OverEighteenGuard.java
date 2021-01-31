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

import com.headstartech.sermo.statemachine.guards.GuardBase;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class OverEighteenGuard extends GuardBase<States, SubscriberEvent> {

    @Override
    protected boolean doEvaluate(StateContext<States, SubscriberEvent> context, String input) {
        try {
            long v = Long.valueOf(input);
            return v >= 18;
        } catch (NumberFormatException e) {
            // ignore
        }
        return false;
    }

}
