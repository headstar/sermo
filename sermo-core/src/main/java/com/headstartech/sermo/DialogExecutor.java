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

package com.headstartech.sermo;

import org.springframework.statemachine.action.Action;

/**
 * Dialog excutor interface.
 *
 * A Dialog session is completed either if reaching an end state or if an {@link Action} throws an exception.
 *
 * @author Per Johansson
 */
public interface DialogExecutor<S, E extends DialogEvent> {

    /**
     * Applies an event for a session.
     *
     * If an exception is thrown by an {@code Action}, it's re-thrown out of this method.
     *
     * @param sessionId
     * @param event
     * @return the {@link DialogEventResult}
     * @throws DialogPersisterException if there is an error loading/persisting the dialog state
     * @throws RuntimeException thrown by {@link Action}s.
     */
    DialogEventResult applyEvent(String sessionId, E event);

    /**
     * Adds the dialog listener.
     *
     * @param listener the listener
     */
    void addListener(DialogListener<E> listener);


    /**
     * Removes the dialog listener.
     *
     * @param listener the listener
     */
    void removeListener(DialogListener<E> listener);

}