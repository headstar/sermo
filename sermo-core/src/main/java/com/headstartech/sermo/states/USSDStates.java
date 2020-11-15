package com.headstartech.sermo.states;


import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.screen.DefaultPagedMenuSupport;
import com.headstartech.sermo.screen.PagedMenuSupport;
import com.headstartech.sermo.statemachine.actions.ExecuteItemHandlerAction;
import com.headstartech.sermo.statemachine.actions.PagedMenuEntryAction;
import com.headstartech.sermo.statemachine.actions.PagedMenuInternalAction;
import com.headstartech.sermo.statemachine.actions.PagedMenuSetupProvider;
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

    public static <S, E extends DialogEvent> PagedUSSDState<S, E> pagedMenuState(S id, PagedMenuSetupProvider<S, E> pagedMenuSetupProvider) {
        return pagedMenuState(id, pagedMenuSetupProvider, new DefaultPagedMenuSupport());
    }

    public static <S, E extends DialogEvent> PagedUSSDState<S, E> pagedMenuState(S id, PagedMenuSetupProvider<S, E> pagedMenuSetupProvider, PagedMenuSupport pagedMenuSupport) {
        DefaultUSSDState.Builder<S,E> builder = DefaultUSSDState.<S, E>builder(id);
        builder.withEntryAction(new PagedMenuEntryAction<>(pagedMenuSetupProvider, pagedMenuSupport));
        builder.withExitAction(new ExecuteItemHandlerAction<>());
        return new DefaultPagedUSSDState<>(builder.build(), new PagedMenuInternalAction<>());
    }

}
