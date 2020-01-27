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

import java.util.Map;

/**
 * @author Per Johansson
 */
public class USSDApplicationBuilder {

    private final StateMachineBuilder.Builder<String, Object> builder;
    private final StateConfigurer<String, Object> stateConfigurer;
    private final StateMachineTransitionConfigurer<String, Object> transitionConfigurer;

    USSDApplicationBuilder() throws Exception {
        this.builder = StateMachineBuilder.builder();
        this.stateConfigurer = builder.configureStates().withStates();
        this.transitionConfigurer = builder.configureTransitions();
    }

    public static USSDApplicationBuilder builder() throws Exception {
        return new USSDApplicationBuilder();
    }

    public USSDApplicationBuilder withInitialState(USSDState ussdState) throws Exception {
        withState(ussdState, true);
        return this;
    }

    public USSDApplicationBuilder withState(USSDState ussdState) throws Exception {
        withState(ussdState, false);
        return this;
    }

    public USSDApplicationBuilder withMenuTransition(USSDState from, USSDState to, Object transitionKey) throws Exception {
        if(from != to) {
            transitionConfigurer
                .withExternal()
                .source(from.getId()).target(to.getId())
                .event(MOInput.INSTANCE)
                .guard(createMenuItemGuard(transitionKey));
        } else {
            transitionConfigurer
                    .withInternal()
                    .source(from.getId()).event(MOInput.INSTANCE)
                    .guard(createMenuItemGuard(transitionKey))
                    .action(new StateWrapperAction(from, StateWrapperAction.ActionEnum.INTERNAL));
        }
        return this;
    }

    public USSDApplicationBuilder withMenuTransition(USSDState from, USSDState to, USSDAction action, Object transitionKey) throws Exception {
        transitionConfigurer
                .withExternal()
                .source(from.getId()).target(to.getId())
                .event(MOInput.INSTANCE)
                .guard(createMenuItemGuard(transitionKey))
                .action(new ActionWrapperAction(action));
        return this;
    }

    public USSDApplication build() throws Exception {
        builder.configureConfiguration()
                .withConfiguration()
                .machineId("atm")
                .listener(listener());
        StateMachine<String, Object> sm = builder.build();
        return new USSDApplication(sm);
    }

    private void withState(USSDState ussdState, boolean initial) throws Exception {
        stateConfigurer
                    .state(ussdState.getId(),
                            new StateWrapperAction(ussdState, StateWrapperAction.ActionEnum.ENTRY),
                            new StateWrapperAction(ussdState, StateWrapperAction.ActionEnum.EXIT));

        if(initial) {
            stateConfigurer.initial(ussdState.getId());
        }

        if(ussdState instanceof PagedScreenUSSDState) {
            withMenuTransition(ussdState, ussdState, ExtendedStateKeys.NEXT_PAGE_KEY);
            withMenuTransition(ussdState, ussdState, ExtendedStateKeys.PREVIOUS_PAGE_KEY);
        }
    }

    private static Guard<String, Object> createNotAnyMenuItemGuard() {
        return (context) -> {
            boolean guard = true;
            Object event = context.getEvent();
            if(event instanceof MOInput) {
                Map<Object, Object> inputTransitionKeyMap = ExtendedStateKeys.getInputTransitionKeyMap(context.getExtendedState());
                if (inputTransitionKeyMap != null) {
                    MOInput MOInput = (MOInput) event;
                    guard = !inputTransitionKeyMap.containsKey(MOInput.getInput());
                }
            }
            return guard;
        };
    }

    private static Guard<String, Object> createMenuItemGuard(Object key) {
        return (context) -> {
            Map<Object, Object> inputTransitionKeyMap = ExtendedStateKeys.getInputTransitionKeyMap(context.getExtendedState());
            Object event = context.getEvent();
            boolean res = false;
            if(event instanceof MOInput) {
                MOInput moInput = (MOInput) event;
                res = key.equals(inputTransitionKeyMap.get(moInput.getInput()));
            }
            return res;
        };
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
        };
    }

}
