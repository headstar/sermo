package com.headstartech.sermo;

import org.springframework.messaging.Message;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Map;

import static com.headstartech.sermo.ExtendedStateKeys.INPUT_ITEM_KEY;

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
        transitionConfigurer
                .withExternal()
                .source(from.getId()).target(to.getId())
                .event(MOInput.INSTANCE)
                .guard(createMenuItemGuard(transitionKey));
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

        transitionConfigurer
                    .withInternal()
                    .source(ussdState.getId()).event(MOInput.INSTANCE)
                .guard(createNotAnyMenuItemGuard())
                .action(new StateWrapperAction(ussdState, StateWrapperAction.ActionEnum.EVENT));
    }

    private static Guard<String, Object> createNotAnyMenuItemGuard() {
        return (context) -> {
            boolean guard = true;
            Object event = context.getEvent();
            if(event instanceof MOInput) {
                Map<Object, Object> inputTransitionKeyMap = getInputTransitionKeyMap(context.getExtendedState());
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
            Map<Object, Object> inputTransitionKeyMap = getInputTransitionKeyMap(context.getExtendedState());
            Object event = context.getEvent();
            boolean res = false;
            if(event instanceof MOInput) {
                MOInput moInput = (MOInput) event;
                res = key.equals(inputTransitionKeyMap.get(moInput.getInput()));
            }
            return res;
        };
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, Object> getInputTransitionKeyMap(ExtendedState extendedState) {
        return (Map<Object, Object>)extendedState.get(ExtendedStateKeys.INPUT_TRANSITION_MAP, Map.class);
    }

    static class StateWrapperAction implements Action<String, Object> {

        public enum ActionEnum { ENTRY, EXIT, EVENT}

        private final USSDState delegate;
        private final ActionEnum actionEnum;

        public StateWrapperAction(USSDState delegate, ActionEnum actionEnum) {
            this.delegate = delegate;
            this.actionEnum = actionEnum;
        }

        @Override
        public void execute(StateContext<String, Object> context) {
            USSDSupport ussdSupport = context.getExtendedState().get(ExtendedStateKeys.SUPPORT_KEY, USSDSupport.class);
            String output = null;
            if(ActionEnum.ENTRY.equals(actionEnum)) {
                output = delegate.onEntry(ussdSupport);
            } else if(ActionEnum.EXIT.equals(actionEnum)) {
                delegate.onExit(ussdSupport);
                transferItemKey(context);
            } else if(ActionEnum.EVENT.equals(actionEnum)) {
                output = delegate.onEvent(ussdSupport, context.getEvent());
            } else {
                throw new IllegalStateException("unknown enum " + actionEnum.name());
            }
            if(output != null) {
                context.getExtendedState().getVariables().put(ExtendedStateKeys.OUTPUT_KEY, output);
            }
        }

        private void transferItemKey(StateContext<String, Object> context) {
            Map<Object, Object> inputItemMap = getInputTransitionKeyMap(context.getExtendedState());
            if(inputItemMap != null) {
                Object event = context.getEvent();
                if (event instanceof MOInput) {
                    MOInput moInput = (MOInput) event;
                    Object inputItemKey = inputItemMap.get(moInput.getInput());
                    if(inputItemKey != null) {
                        context.getExtendedState().getVariables().put(INPUT_ITEM_KEY, inputItemKey);
                    }
                }
            }
        }
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
