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

package com.headstartech.sermo.statemachine;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.statemachine.guards.ScreenTransitionGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * {@link StateMachineListener} implementation logging state transitions.
 *
 * @author Per Johansson
 */
public class LoggingStateMachineListener<S, E extends DialogEvent> extends StateMachineListenerAdapter<S, E> {

    private static final Logger log = LoggerFactory.getLogger(LoggingStateMachineListener.class);

    @Override
    public void stateChanged(State<S, E> from, State<S, E> to) {
        log.debug("State changed: from={}, to={}", from.getId(), to.getId());
    }

    @Override
    public void stateEntered(State<S, E> state) {
        log.debug("State entered: state={}", state.getId());
    }

    @Override
    public void stateExited(State<S, E> state) {
        log.debug("State exited: state={}", state.getId());
    }

    @Override
    public void transition(Transition<S, E> transition) {
        if(hasScreenTransitionGuard(transition)) {
            log.debug("Screen transition: transitionId={}, source={}, target={}", getTransitionId(transition), transition.getSource().getId(), transition.getTarget().getId());
        } else {
            log.debug("Transition: source={}, target={}", transition.getSource().getId(), transition.getTarget().getId());
        }
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        log.trace("Extended state changed: key={}, value={}", key, value);
    }

    @Override
    public void eventNotAccepted(Message<E> event) {
        log.warn("Event not accepted: event={}", event);
    }

    private Object getTransitionId(Transition<S, E> transition) {
        if(hasScreenTransitionGuard(transition)) {
            return ((ScreenTransitionGuard<S, E>) transition.getGuard()).getTransitionId();
        } else {
            return null;
        }
    }

    private boolean hasScreenTransitionGuard(Transition<S,E> transition) {
        return transition.getGuard() instanceof ScreenTransitionGuard;
    }

}
