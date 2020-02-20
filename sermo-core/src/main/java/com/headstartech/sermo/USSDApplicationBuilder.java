package com.headstartech.sermo;

import com.headstartech.sermo.actions.SetStateMachineErrorOnExceptionAction;
import com.headstartech.sermo.guards.FormInputGuard;
import com.headstartech.sermo.guards.InitialTransitionGuard;
import com.headstartech.sermo.guards.ScreenTransitionGuard;
import com.headstartech.sermo.states.PagedUSSDState;
import com.headstartech.sermo.states.USSDEndState;
import com.headstartech.sermo.states.USSDState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.action.Actions;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class USSDApplicationBuilder {

    public static <S, E extends MOInput> USSDApplicationBuilder.Builder<S, E> builder(StateMachineFactoryBuilder.Builder<S, E> stateMachineFactoryBuilder, Class<E> clazz) throws Exception {
        return new USSDApplicationBuilder.Builder<S, E>(stateMachineFactoryBuilder.configureStates().withStates(), stateMachineFactoryBuilder.configureTransitions(), clazz.newInstance());
    }
    public static <S, E extends MOInput> USSDApplicationBuilder.Builder<S, E> builder(StateConfigurer<S, E> stateConfigurer, StateMachineTransitionConfigurer<S, E> transitionConfigurer, Class<E> clazz) throws IllegalAccessException, InstantiationException {
        return new USSDApplicationBuilder.Builder<S, E>(stateConfigurer, transitionConfigurer, clazz.newInstance());
    }

    public static class Builder<S, E extends MOInput> {

        private final StateConfigurer<S, E> stateConfigurer;
        private final StateMachineTransitionConfigurer<S, E> transitionConfigurer;
        private S initialState;
        private final E eventToken;
        private CompositeAction<S, E> compositeAction = new CompositeAction<>(new SetStateMachineErrorOnExceptionAction<>());

        public Builder(StateConfigurer<S, E> stateConfigurer, StateMachineTransitionConfigurer<S, E> transitionConfigurer, E eventToken) {
            this.stateConfigurer = stateConfigurer;
            this.transitionConfigurer = transitionConfigurer;
            this.eventToken = eventToken;
        }

        public Builder<S, E> withInitialState(S state) {
            stateConfigurer.state(state);
            stateConfigurer.initial(state);
            initialState = state;
            return this;
        }

        public Builder<S, E> withState(USSDState<S, E> state) throws Exception {
            stateConfigurer.state(state.getId(), wrapWithErrorActions(state.getEntryActions()), wrapWithErrorActions(state.getExitActions()));

            if (state instanceof PagedUSSDState) {
                withPagedScreenTransitions(state.getId(), ((PagedUSSDState<S, E>) state).toNextOrToPreviousPageAction());
            }
            return this;
        }

        public Builder<S, E> withEndState(USSDEndState<S, E> state) throws Exception {
            stateConfigurer.state(state.getId(), wrapWithErrorActions(state.getEntryActions()));
            stateConfigurer.end(state.getId());
            return this;
        }

        public Builder<S, E> withShortCodeTransition(S to, Pattern shortCode) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(initialState)
                    .target(to)
                    .event(eventToken)
                    .guard(new InitialTransitionGuard<>(shortCode));

            return this;
        }

        public Builder<S, E> withFormInputTransition(S from, S to, Predicate<String> predicate) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(new FormInputGuard<>(predicate));

            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(from)
                    .event(eventToken)
                    .guard(new FormInputGuard<>(predicate.negate()));
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

        public Builder<S, E> withScreenTransition(S from, S to, Object transition) throws Exception {
            transitionConfigurer
                    .withExternal()
                    .source(from)
                    .target(to)
                    .event(eventToken)
                    .guard(screenTransitionGuard(transition));

            return this;
        }

        public Builder<S, E> withErrorAction(Action<S, E> action) {
            compositeAction.setErrorAction(action);
            return this;
        }

        private Builder<S, E> withPagedScreenTransitions(S state, Action<S, E> nextPreviousPageAction) throws Exception {
            transitionConfigurer
                    .withInternal()
                    .source(state)
                    .event(eventToken)
                    .guard(screenTransitionGuard(ExtendedStateKeys.NEXT_PAGE_KEY))
                    .action(wrapWithErrorActions(nextPreviousPageAction));

            transitionConfigurer
                    .withInternal()
                    .source(state)
                    .event(eventToken)
                    .guard(screenTransitionGuard(ExtendedStateKeys.PREVIOUS_PAGE_KEY))
                    .action(wrapWithErrorActions(nextPreviousPageAction));
            return this;
        }

        private Guard<S, E> screenTransitionGuard(Object transition) {
            return new ScreenTransitionGuard<>(transition);
        }

        private Collection<Action<S, E>> wrapWithErrorActions(Collection<Action<S, E>> actions) {
            return actions.stream().map(e -> wrapWithErrorActions(e)).collect(Collectors.toCollection(ArrayList::new));
        }


        private Action<S, E> wrapWithErrorActions(Action<S, E> action) {
            return Actions.errorCallingAction(action, compositeAction);
        }

    }


    private static class CompositeAction<S, E> implements Action<S, E> {

        private static final Log log = LogFactory.getLog(CompositeAction.class);

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
