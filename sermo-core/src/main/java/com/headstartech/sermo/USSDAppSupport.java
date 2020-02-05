package com.headstartech.sermo;

import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.regex.Pattern;

/**
 * @author Per Johansson
 */
public class USSDAppSupport {

    private static final String INITIAL_STATE_ID = "_INITIAL_STATE_ID_";


    public static void init(StateConfigurer<String, Object> stateConfigurer) {
        stateConfigurer.state(INITIAL_STATE_ID);
        stateConfigurer.initial(INITIAL_STATE_ID);
    }

    public static void withShortCodeTransition(StateMachineTransitionConfigurer<String, Object> transitionConfigurer, String to, Pattern pattern) throws Exception {
        transitionConfigurer
                .withExternal()
                .source(INITIAL_STATE_ID).target(to)
                .event(MOInput.INSTANCE)
                .guard(new InitialTransitionGuard(pattern));
    }

    public static void withScreenTransition(StateMachineTransitionConfigurer<String, Object> transitionConfigurer, String from, String to, Object transition) throws Exception {
        transitionConfigurer
                .withExternal()
                .source(from).target(to)
                .event(MOInput.INSTANCE)
                .guard(createScreenTransitionGuard(transition));
    }

    public static void withPagedScreenTransitions(StateMachineTransitionConfigurer<String, Object> transitionConfigurer, String state, Action<String, Object> nextPreviousPageAction) throws Exception {
        transitionConfigurer
                .withInternal()
                .source(state).event(MOInput.INSTANCE)
                .guard(createScreenTransitionGuard(ExtendedStateKeys.NEXT_PAGE_KEY))
                .action(nextPreviousPageAction);

        transitionConfigurer
                .withInternal()
                .source(state).event(MOInput.INSTANCE)
                .guard(createScreenTransitionGuard(ExtendedStateKeys.PREVIOUS_PAGE_KEY))
                .action(nextPreviousPageAction);
    }

    private static Guard<String, Object> createScreenTransitionGuard(Object transition) {
        return new ScreenTransitionGuard(transition);
    }
}
