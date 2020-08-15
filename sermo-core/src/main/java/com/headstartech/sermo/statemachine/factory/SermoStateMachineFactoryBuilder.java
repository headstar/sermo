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

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.SermoSystemConstants;
import com.headstartech.sermo.statemachine.LoggingStateMachineListener;
import com.headstartech.sermo.statemachine.guards.PredicateInputGuard;
import com.headstartech.sermo.statemachine.guards.ScreenTransitionGuard;
import com.headstartech.sermo.states.PagedUSSDState;
import com.headstartech.sermo.states.USSDState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.action.Actions;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.ChoiceTransitionConfigurer;
import org.springframework.statemachine.config.configurers.ConfigurationConfigurer;
import org.springframework.statemachine.config.configurers.ExternalTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class SermoStateMachineFactoryBuilder {

    public static <S, E extends DialogEvent> SermoStateMachineFactoryBuilder.Builder<S, E> builder(StateMachineFactoryBuilder.Builder<S, E> stateMachineFactoryBuilder, Class<E> clazz) throws Exception {
        return new SermoStateMachineFactoryBuilder.Builder<>(stateMachineFactoryBuilder, clazz.newInstance());
    }

    public static <S, E extends DialogEvent> SermoStateMachineFactoryBuilder.Builder<S, E> builder(Class<E> clazz) throws Exception {
        return new SermoStateMachineFactoryBuilder.Builder<>(StateMachineFactoryBuilder.builder(), clazz.newInstance());
    }

    public static class Builder<S, E extends DialogEvent> {

        private final StateMachineFactoryBuilder.Builder<S, E> stateMachineFactoryBuilder;
        private final StateConfigurer<S, E> stateConfigurer;
        private final StateMachineTransitionConfigurer<S, E> transitionConfigurer;
        private final ConfigurationConfigurer<S, E> configurationConfigurer;
        private S initialState;
        private final E eventToken;
        private CompositeAction<S, E> compositeAction = new CompositeAction<>(new SetStateMachineErrorOnExceptionAction<>());

        public Builder(StateMachineFactoryBuilder.Builder<S, E> stateMachineFactoryBuilder, E eventToken) throws Exception {
            this.stateMachineFactoryBuilder = stateMachineFactoryBuilder;
            this.configurationConfigurer = stateMachineFactoryBuilder.configureConfiguration().withConfiguration();
            this.stateConfigurer = stateMachineFactoryBuilder.configureStates().withStates();
            this.transitionConfigurer = stateMachineFactoryBuilder.configureTransitions();
            this.eventToken = eventToken;
        }

        public Builder<S, E> withListener(StateMachineListener<S, E> stateMachineListener) {
            configurationConfigurer.listener(stateMachineListener);
            return this;
        }

        public Builder<S, E> withLoggingListener() {
            return withListener(new LoggingStateMachineListener<>());
        }


        public Builder<S, E> withInitialState(S state) {
            stateConfigurer.state(state);
            stateConfigurer.initial(state);
            initialState = state;
            return this;
        }

        public Builder<S, E> withStates(Collection<USSDState<S, E>> states) throws Exception {
            for (USSDState<S, E> state : states) {
                withState(state);
            }
            return this;
        }

        public Builder<S, E> withState(USSDState<S, E> state) throws Exception {
            stateConfigurer.state(state.getId(), wrapWithErrorActions(state.getEntryActions()), wrapWithErrorActions(state.getExitActions()));

            if (state instanceof PagedUSSDState) {
                // TODO: anyway to avoid using @SuppressWarnings ?
                @SuppressWarnings("unchecked")
                PagedUSSDState<S,E> pagedUSSDState = ((PagedUSSDState<S, E>) state);
                withPagedScreenTransitions(state.getId(), pagedUSSDState.toNextOrToPreviousPageAction());
            }

            if(state.isEnd()) {
                stateConfigurer.end(state.getId());
            }
            return this;
        }

        public Builder<S, E> withChoice(S s, S defaultTarget, Collection<ChoiceOption<S, E>> options) throws Exception {
           return withChoice(s, new ChoiceOption<>(defaultTarget, null), options);
        }

        public Builder<S, E> withChoice(S s, ChoiceOption<S, E> defaultOption, Collection<ChoiceOption<S, E>> options) throws Exception {
            stateConfigurer.choice(s);
            ChoiceTransitionConfigurer<S, E> choiceTransitionConfigurer = transitionConfigurer.withChoice();
            choiceTransitionConfigurer.source(s);
            options.forEach(e -> {
                if(e.getAction() != null) {
                    choiceTransitionConfigurer.then(e.getTarget(), e.getGuard(), wrapWithErrorActions(e.getAction()));
                } else {
                    choiceTransitionConfigurer.then(e.getTarget(), e.getGuard());
                }
            });
            if(defaultOption.getAction() != null) {
                choiceTransitionConfigurer.last(defaultOption.getTarget(), wrapWithErrorActions(defaultOption.getAction()));
            } else {
                choiceTransitionConfigurer.last(defaultOption.getTarget());
            }
            return this;
        }

        public Builder<S, E> withInitialTransition(S to, Guard<S,E> guard) throws Exception {
            return withInitialTransition(to, guard, null);
        }

        public Builder<S, E> withInitialTransition(S to, Guard<S,E> guard, Action<S, E> action) throws Exception {
            ExternalTransitionConfigurer<S, E> externalTransitionConfigurer = transitionConfigurer
                    .withExternal()
                    .source(initialState)
                    .target(to)
                    .event(eventToken)
                    .guard(guard);

            if(action != null) {
                externalTransitionConfigurer.action(wrapWithErrorActions(action));
            }

            return this;
        }

        public Builder<S, E> withFormInputTransition(S from, S to, Predicate<String> inputValid) throws Exception {
            withFormInputTransition(from, to, inputValid, null, null);
            return this;
        }

        public Builder<S, E> withFormInputTransition(S from, S to, Predicate<String> inputValid, Action<S, E> inputValidAction, Action<S, E> inputInvalidAction) throws Exception {
            ExternalTransitionConfigurer<S, E> externalTransitionConfigurer = transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(new PredicateInputGuard<>(inputValid));

            if(inputValidAction != null) {
                externalTransitionConfigurer.action(wrapWithErrorActions(inputValidAction));
            }

            externalTransitionConfigurer = transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(from)
                    .event(eventToken)
                    .guard(new PredicateInputGuard<>(inputValid.negate()));

            if(inputInvalidAction != null) {
                externalTransitionConfigurer.action(wrapWithErrorActions(inputInvalidAction));
            }
            return this;
        }

        public Builder<S, E> withTransition(S from, S to, Guard<S,E> guard, Action<S, E> action) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(guard)
                    .action(wrapWithErrorActions(action));
            return this;
        }

        public Builder<S, E> withTransition(S from, S to, Guard<S,E> guard) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(guard);
            return this;
        }

        public Builder<S, E> withScreenTransition(S from, S to, Object transitionId) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(screenTransitionGuard(transitionId));

            return this;
        }

        public Builder<S, E> withScreenTransition(S from, S to, Object transitionId, Action<S, E> action) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(screenTransitionGuard(transitionId))
                    .action(wrapWithErrorActions(action));
            return this;
        }

        public Builder<S, E> withErrorAction(Action<S, E> action) {
            compositeAction.setErrorAction(action);
            return this;
        }

        public StateMachineFactory<S, E> build() {
            return stateMachineFactoryBuilder.build();
        }

        private Builder<S, E> withPagedScreenTransitions(S state, Action<S, E> nextPreviousPageAction) throws Exception {
            transitionConfigurer
                    .withInternal()
                    .source(state)
                    .event(eventToken)
                    .guard(screenTransitionGuard(SermoSystemConstants.NEXT_PAGE_KEY))
                    .action(wrapWithErrorActions(nextPreviousPageAction));

            transitionConfigurer
                    .withInternal()
                    .source(state)
                    .event(eventToken)
                    .guard(screenTransitionGuard(SermoSystemConstants.PREVIOUS_PAGE_KEY))
                    .action(wrapWithErrorActions(nextPreviousPageAction));
            return this;
        }

        private Guard<S, E> screenTransitionGuard(Object transitionId) {
            return new ScreenTransitionGuard<>(transitionId);
        }

        private Collection<Action<S, E>> wrapWithErrorActions(Collection<Action<S, E>> actions) {
            return actions.stream().map(this::wrapWithErrorActions).collect(Collectors.toCollection(ArrayList::new));
        }


        private Action<S, E> wrapWithErrorActions(Action<S, E> action) {
            return Actions.errorCallingAction(action, compositeAction);
        }

    }


    private static class CompositeAction<S, E> implements Action<S, E> {

        private static final Logger log = LoggerFactory.getLogger(CompositeAction.class);

        private final Action<S, E> setMachineOnErrorAction;
        private Action<S, E> errorAction;

        public CompositeAction(Action<S, E> setMachineOnErrorAction) {
            this.setMachineOnErrorAction = setMachineOnErrorAction;
        }

        @Override
        public void execute(StateContext<S, E> context) {
            setMachineOnErrorAction.execute(context);
            if (errorAction != null) {
                try {
                    errorAction.execute(context);
                } catch (Exception e) {
                    log.warn("Error action threw exception:", e);
                }
            }
        }

        public void setErrorAction(Action<S, E> errorAction) {
            this.errorAction = errorAction;
        }
    }


}
