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

package com.headstartech.sermo.statemachine.factory;

import org.springframework.statemachine.action.Action;

/**
 * Class representing the default option when configuring a "choice" construct.
 *
 * @author Per Johansson
 */
public class DefaultChoiceOption<S, E> extends ChoiceOption<S, E> {

    public DefaultChoiceOption(S target) {
        super(target, null, null);
    }

    public DefaultChoiceOption(S target, Action<S, E> action) {
        super(target, null, action);
    }

}
