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

import com.headstartech.sermo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Per Johansson
 */
public class CompositeDialogListener<E extends DialogEvent> extends AbstractCompositeListener<DialogListener<E>> implements DialogListener<E> {

    private static final Logger log = LoggerFactory.getLogger(DialogExecutor.class);

    @Override
    public void preEventHandled(String sessionId, E event) {
        getListeners().stream().forEach(e ->
                {
                    try {
                        e.preEventHandled(sessionId, event);
                    } catch(Throwable ex) {
                        log.warn("Error during preEventHandled", ex);
                    }
                }
        );
    }

    @Override
    public void postEventHandled(String sessionId, E event, DialogException sde) {
        getListeners().stream().forEach(e ->
                {
                    try {
                        e.postEventHandled(sessionId, event, sde);
                    } catch(Throwable ex) {
                        log.warn("Error during postEventHandled", ex);
                    }
                }
        );
    }
}
