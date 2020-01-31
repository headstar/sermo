package com.headstartech.sermo;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.regex.Pattern;

/**
 * @author Per Johansson
 */
public class USSDApplicationBuilder {

    private static final String INITIAL_STATE_ID = "_INITIAL_STATE_ID_";

//    private final StateMachineBuilder.Builder<String, Object> builder;
    private final StateConfigurer<String, Object> stateConfigurer;
    private final StateMachineTransitionConfigurer<String, Object> transitionConfigurer;

    private final StateMachineFactoryBuilder.Builder<String, Object> builder;

    USSDApplicationBuilder() throws Exception {
        this.builder = StateMachineFactoryBuilder.builder();
        this.stateConfigurer = builder.configureStates().withStates();
        this.transitionConfigurer = builder.configureTransitions();

        stateConfigurer.state(INITIAL_STATE_ID);
        stateConfigurer.initial(INITIAL_STATE_ID);
    }

    public static USSDApplicationBuilder builder() throws Exception {
        return new USSDApplicationBuilder();
    }

    public USSDApplicationBuilder withInitialTransition(USSDState to, Pattern pattern) throws Exception {
        transitionConfigurer
                .withExternal()
                .source(INITIAL_STATE_ID).target(to.getId())
                .event(MOInput.INSTANCE)
                .guard(new InitialTransitionGuard(pattern));
        return this;
    }

    public USSDApplicationBuilder withState(USSDState ussdState) throws Exception {
        withState(ussdState, false);
        return this;
    }

    public USSDApplicationBuilder withMenuTransition(USSDState from, USSDState to, Object transitionName) throws Exception {
        if(from != to) {
            transitionConfigurer
                .withExternal()
                .source(from.getId()).target(to.getId())
                .event(MOInput.INSTANCE)
                .guard(createMenuItemGuard(transitionName));
        } else {
            transitionConfigurer
                    .withInternal()
                    .source(from.getId()).event(MOInput.INSTANCE)
                    .guard(createMenuItemGuard(transitionName))
                    .action(new StateWrapperAction(from, StateWrapperAction.ActionEnum.INTERNAL));
        }
        return this;
    }

    public USSDApplicationBuilder withMenuTransition(USSDState from, USSDState to, USSDAction action, Object transitionName) throws Exception {
        transitionConfigurer
                .withExternal()
                .source(from.getId()).target(to.getId())
                .event(MOInput.INSTANCE)
                .guard(createMenuItemGuard(transitionName))
                .action(new ActionWrapperAction(action));
        return this;
    }

    public USSDApplication build() throws Exception {
        builder.configureConfiguration()
                .withConfiguration()
                .listener(listener());
        StateMachine<String, Object> sm = builder.build().getStateMachine();
        return new USSDApplication(sm);
    }

    private void withState(USSDState ussdState, boolean initial) throws Exception {
        stateConfigurer
                    .state(ussdState.getId(),
                            new StateWrapperAction(ussdState, StateWrapperAction.ActionEnum.ENTRY),
                            new StateWrapperAction(ussdState, StateWrapperAction.ActionEnum.EXIT));

        if(ussdState instanceof PagedScreenUSSDState) {
            withMenuTransition(ussdState, ussdState, ExtendedStateKeys.NEXT_PAGE_KEY);
            withMenuTransition(ussdState, ussdState, ExtendedStateKeys.PREVIOUS_PAGE_KEY);
        }
    }

    private static Guard<String, Object> createMenuItemGuard(Object transitionName) {
        return new MenuItemGuard(transitionName);
    }

    private static StateMachineListener<String, Object> listener() {
        return new StateMachineListenerAdapter<String, Object>() {
            @Override
            public void stateChanged(State<String, Object> from, State<String, Object> to) {
                System.out.println("State change to " + to.getId());
            }

            @Override
            public void eventNotAccepted(Message<Object> event) {
                System.out.println("Event not accepted" + event);
            }

            @Override
            public void stateMachineError(StateMachine<String, Object> stateMachine, Exception exception) {
                System.out.println("State machine error " + exception);
            }
        };
    }

}
