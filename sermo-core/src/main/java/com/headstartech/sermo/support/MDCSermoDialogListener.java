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

package com.headstartech.sermo.support;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.SermoDialogException;
import com.headstartech.sermo.SermoDialogListener;
import org.slf4j.MDC;

import static com.headstartech.sermo.SermoSystemConstants.MDC_SESSION_ID_KEY;

/**
 * {@link SermoDialogListener} implementation setting the session id in the MDC to be available in the log output.
 *
 * @author Per Johansson
 */
public class MDCSermoDialogListener<E extends DialogEvent> implements SermoDialogListener<E> {

    @Override
    public void preEventHandled(String sessionId, E event) {
        MDC.put(MDC_SESSION_ID_KEY, sessionId);
    }

    @Override
    public void postEventHandled(String sessionId, E event, SermoDialogException e) {
        MDC.clear();
    }
}
