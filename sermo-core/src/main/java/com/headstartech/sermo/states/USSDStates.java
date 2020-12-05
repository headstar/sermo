package com.headstartech.sermo.states;


import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.DefaultPagedScreenSupport;
import com.headstartech.sermo.screen.PagedScreenSupport;
import com.headstartech.sermo.statemachine.actions.*;
import org.springframework.statemachine.action.Action;

/**
 * Convenience class to create common USSD states.
 *
 */
public class USSDStates {

    private USSDStates() {
    }

    public static <S, E extends DialogEvent> USSDState<S, E> menuState(S id, Action<S, E> entryAction) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEntryAction(entryAction);
        builder.withExitAction(new ExecuteItemHandlerAction<>());
        return builder.build();
    }

    public static <S, E extends DialogEvent> USSDState<S, E> menuState(S id, Action<S, E> entryAction,
                                                                       Action<S, E> exitAction) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEntryAction(entryAction);
        builder.withExitAction(new ExecuteItemHandlerAction<>());
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

    public static <S, E extends DialogEvent> PagedUSSDState<S, E> pagedScreenState(S id, PagedScreenSetupProvider<S, E> pagedScreenSetupProvider) {
        return pagedScreenState(id, pagedScreenSetupProvider, new DefaultPagedScreenSupport());
    }

    public static <S, E extends DialogEvent> PagedUSSDState<S, E> pagedScreenState(S id, PagedScreenSetupProvider<S, E> pagedScreenSetupProvider, PagedScreenSupport pagedScreenSupport) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEntryAction(new DefaultPagedScreenEntryAction<>(pagedScreenSetupProvider, pagedScreenSupport));
        builder.withExitAction(new ExecuteItemHandlerAction<>());
        return new DefaultPagedUSSDState<>(builder.build(), new DefaultPagedScreenInternalAction<>(pagedScreenSupport));
    }

}
