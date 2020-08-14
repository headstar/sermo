package com.headstartech.sermo.states;


import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.statemachine.actions.ItemHandlerExitAction;
import com.headstartech.sermo.statemachine.actions.PagedMenuScreenInternalAction;
import org.springframework.statemachine.action.Action;

/**
 * Convenience class to create common USSD states.
 *
 */
public class USSDStates {

    private USSDStates() {
    }

    public static <S, E extends DialogEvent> USSDState<S, E> menuInputState(S id, Action<S, E> entryAction) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEntryAction(entryAction);
        builder.withExitAction(new ItemHandlerExitAction<>());
        return builder.build();
    }

    public static <S, E extends DialogEvent> USSDState<S, E> menuInputState(S id, Action<S, E> entryAction,
                                                                                  Action<S, E> exitAction) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEntryAction(entryAction);
        builder.withExitAction(new ItemHandlerExitAction<>());
        builder.withExitAction(exitAction);
        return builder.build();
    }

    public static <S, E extends DialogEvent> USSDState<S, E> endState(S id) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEnd(true);
        return builder.build();
    }

    public static <S, E extends DialogEvent> USSDState<S, E> endState(S id, Action<S, E> entryAction) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEntryAction(entryAction);
        builder.withEnd(true);
        return builder.build();
    }

    public static <S, E extends DialogEvent> IPagedUSSDState<S, E> pagedMenuInputState(S id, Action<S, E> entryAction) {
        return new XPagedUSSDState<>(id, entryAction, new ItemHandlerExitAction<>(), new PagedMenuScreenInternalAction<>());
    }
}
