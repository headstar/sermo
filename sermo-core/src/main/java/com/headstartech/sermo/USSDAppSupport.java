package com.headstartech.sermo;

import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.action.Actions;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class USSDAppSupport {

    public static <S, E> USSDAppSupport.Builder<S, E> builder(StateConfigurer<S, E> stateConfigurer, StateMachineTransitionConfigurer<S, E> transitionConfigurer,
                                                              Function<E, String> eventToInput, E eventToken) {
        return new USSDAppSupport.Builder<>(stateConfigurer, transitionConfigurer, eventToInput, eventToken);
    }

    public static class Builder<S, E> {

        private final StateConfigurer<S, E> stateConfigurer;
        private final StateMachineTransitionConfigurer<S, E> transitionConfigurer;
        private S initialState;
        private final Function<E, String> eventToInput;
        private final E eventToken;
        private Action<S, E> errorAction = new SetStateMachineErrorOnExceptionAction<>();

        public Builder(StateConfigurer<S, E> stateConfigurer, StateMachineTransitionConfigurer<S, E> transitionConfigurer, Function<E, String> eventToInput, E eventToken) {
            this.stateConfigurer = stateConfigurer;
            this.transitionConfigurer = transitionConfigurer;
            this.eventToInput = eventToInput;
            this.eventToken = eventToken;
        }

        public Builder<S, E> withInitialState(S state) {
            stateConfigurer.state(state);
            stateConfigurer.initial(state);
            initialState = state;
            return this;
        }

        public Builder<S, E> withState(USSDState<S, E> state) throws Exception {
            stateConfigurer.state(state.getId(), wrapWithErrorAction(state.getEntryActions()), wrapWithErrorAction(state.getExitActions()));

            if (state instanceof PagedUSSDState) {
                withPagedScreenTransitions(state.getId(), ((PagedUSSDState) state).toNextOrToPreviousPageAction());
            }
            return this;
        }

        public Builder<S, E> withShortCodeTransition(S to, Pattern shortCode) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(initialState)
                    .target(to)
                    .event(eventToken)
                    .guard(new InitialTransitionGuard<>(eventToInput, shortCode));

            return this;
        }

        public Builder<S, E> withFormInputTransition(S from, S to, Predicate<String> predicate) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(new FormInputGuard<>(eventToInput, predicate));

            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(from)
                    .event(eventToken)
                    .guard(new FormInputGuard<>(eventToInput, predicate.negate()));
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

        public Builder<S, E> withScreenTransition(S from, S to, Object transition) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(screenTransitionGuard(eventToInput, transition));

            return this;
        }

        private Builder<S, E> withPagedScreenTransitions(S state, Action<S, E> nextPreviousPageAction) throws Exception {
            transitionConfigurer
                    .withInternal()
                    .source(state)
                    .event(eventToken)
                    .guard(screenTransitionGuard(eventToInput, ExtendedStateKeys.NEXT_PAGE_KEY))
                    .action(wrapWithErrorAction(nextPreviousPageAction));

            transitionConfigurer
                    .withInternal()
                    .source(state)
                    .event(eventToken)
                    .guard(screenTransitionGuard(eventToInput, ExtendedStateKeys.PREVIOUS_PAGE_KEY))
                    .action(wrapWithErrorAction(nextPreviousPageAction));
            return this;
        }

        private Guard<S, E> screenTransitionGuard(Function<E, String> eventToInput, Object transition) {
            return new ScreenTransitionGuard<>(eventToInput, transition);
        }

        private Collection<Action<S, E>> wrapWithErrorAction(Collection<Action<S, E>> actions) {
            return actions.stream().map(e -> wrapWithErrorAction(e)).collect(Collectors.toCollection(ArrayList::new));
        }


        private Action<S, E> wrapWithErrorAction(Action<S, E> action) {
            return errorAction != null ? Actions.errorCallingAction(action, errorAction) : action;
        }

    }



}
