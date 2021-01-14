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
import com.headstartech.sermo.SystemConstants;
import com.headstartech.sermo.statemachine.LoggingStateMachineListener;
import com.headstartech.sermo.statemachine.actions.ExecuteItemHandlerAction;
import com.headstartech.sermo.statemachine.guards.PredicateInputGuard;
import com.headstartech.sermo.statemachine.guards.ScreenTransitionGuard;
import com.headstartech.sermo.states.PagedUSSDState;
import com.headstartech.sermo.states.USSDState;
import org.springframework.statemachine.action.Action;
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
public class DialogStateMachineFactoryBuilder {

    public static <S, E extends DialogEvent> DialogStateMachineFactoryBuilder.Builder<S, E> builder(StateMachineFactoryBuilder.Builder<S, E> stateMachineFactoryBuilder, Class<E> clazz) throws Exception {
        return new DialogStateMachineFactoryBuilder.Builder<>(stateMachineFactoryBuilder, clazz.newInstance());
    }

    public static <S, E extends DialogEvent> DialogStateMachineFactoryBuilder.Builder<S, E> builder(Class<E> clazz) throws Exception {
        return new DialogStateMachineFactoryBuilder.Builder<>(StateMachineFactoryBuilder.builder(), clazz.newInstance());
    }

    public static class Builder<S, E extends DialogEvent> {

        private final StateMachineFactoryBuilder.Builder<S, E> stateMachineFactoryBuilder;
        private final StateConfigurer<S, E> stateConfigurer;
        private final StateMachineTransitionConfigurer<S, E> transitionConfigurer;
        private final ConfigurationConfigurer<S, E> configurationConfigurer;
        private S initialState;
        private final E eventToken;

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

        @SafeVarargs
        public final Builder<S, E> withChoice(S s, S defaultTarget, ChoiceOption<S, E>... options) throws Exception {
           return withChoice(s, new DefaultChoiceOption<>(defaultTarget), options);
        }

        @SafeVarargs
        public final Builder<S, E> withChoice(S s, DefaultChoiceOption<S, E> defaultOption, ChoiceOption<S, E>... options) throws Exception {
            stateConfigurer.choice(s);
            ChoiceTransitionConfigurer<S, E> choiceTransitionConfigurer = transitionConfigurer.withChoice();
            choiceTransitionConfigurer.source(s);
            for(int i=0; i<options.length; ++i) {
                ChoiceOption<S, E> option = options[i];
                if (option.getAction() != null) {
                    choiceTransitionConfigurer.then(option.getTarget(), option.getGuard(), wrapWithErrorAction(option.getAction()));
                } else {
                    choiceTransitionConfigurer.then(option.getTarget(), option.getGuard());
                }
            }
            if(defaultOption.getAction() != null) {
                choiceTransitionConfigurer.last(defaultOption.getTarget(), wrapWithErrorAction(defaultOption.getAction()));
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
                externalTransitionConfigurer.action(wrapWithErrorAction(action));
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
                externalTransitionConfigurer.action(wrapWithErrorAction(inputValidAction));
            }

            externalTransitionConfigurer = transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(from)
                    .event(eventToken)
                    .guard(new PredicateInputGuard<>(inputValid.negate()));

            if(inputInvalidAction != null) {
                externalTransitionConfigurer.action(wrapWithErrorAction(inputInvalidAction));
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
                    .action(wrapWithErrorAction(action));
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
                    .guard(screenTransitionGuard(transitionId))
                    .action(wrapWithErrorAction(new ExecuteItemHandlerAction<>())); // making sure item handler is executed before application code
            return this;
        }

        public Builder<S, E> withScreenTransition(S from, S to, Object transitionId, Action<S, E> action) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(screenTransitionGuard(transitionId))
                    .action(wrapWithErrorAction(new ExecuteItemHandlerAction<>()))  // making sure item handler is executed before application code
                    .action(wrapWithErrorAction(action));
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
                    .guard(screenTransitionGuard(SystemConstants.NEXT_PAGE_KEY))
                    .action(wrapWithErrorAction(nextPreviousPageAction));

            transitionConfigurer
                    .withInternal()
                    .source(state)
                    .event(eventToken)
                    .guard(screenTransitionGuard(SystemConstants.PREVIOUS_PAGE_KEY))
                    .action(wrapWithErrorAction(nextPreviousPageAction));
            return this;
        }

        private Guard<S, E> screenTransitionGuard(Object transitionId) {
            return new ScreenTransitionGuard<>(transitionId);
        }

        private Collection<Action<S, E>> wrapWithErrorActions(Collection<Action<S, E>> actions) {
            return actions.stream().map(this::wrapWithErrorAction).collect(Collectors.toCollection(ArrayList::new));
        }

        private Action<S, E> wrapWithErrorAction(Action<S, E> delegate) {
            return new ErrorHandlingWrapperAction<>(delegate);
        }

    }

}
