package com.headstartech.sermo.support;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.DialogEventResult;
import com.headstartech.sermo.DialogExecutor;
import com.headstartech.sermo.TestUtils;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.screen.MenuGroup;
import com.headstartech.sermo.screen.MenuItem;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.statemachine.actions.OnItemHandler;
import com.headstartech.sermo.statemachine.actions.OnItemHandlers;
import com.headstartech.sermo.statemachine.factory.DialogStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class DefaultDialogExecutorTest {

    @Test
    public void applyEventThrowsWhenActionThrows() throws Exception {
        // given
        StateMachineFactory<TestUtils.States, DialogEvent> stateMachineFactory = createStateMachineThrowingException();
        CachePersist<TestUtils.States, DialogEvent> cachePersist = Mockito.spy(createCachePersist());
        StateMachineService<TestUtils.States, DialogEvent> stateMachineService = new DefaultStateMachineService<>(stateMachineFactory, cachePersist, cachePersist);

        DialogExecutor<TestUtils.States, DialogEvent> dialogExecutor = new DefaultDialogExecutor<TestUtils.States, DialogEvent>(stateMachineService);

        // when
        try {
            dialogExecutor.applyEvent("session1", new DialogEvent("1"));
            fail("Expected exception");
        } catch(Exception e) {
            // then
            assertTrue(e instanceof TestException);
            verify(cachePersist).delete("session1");
        }
    }

    @Test
    public void contextDeletedWhenEndStateReached() throws Exception {
        // given
        String sessionId = "session1";
        StateMachineFactory<TestUtils.States, DialogEvent> stateMachineFactory = createStateMachineToEndState();
        CachePersist<TestUtils.States, DialogEvent> cachePersist = Mockito.spy(createCachePersist());
        InOrder inOrder = inOrder(cachePersist);

        StateMachineService<TestUtils.States, DialogEvent> stateMachineService = new DefaultStateMachineService<>(stateMachineFactory, cachePersist, cachePersist);

        DialogExecutor<TestUtils.States, DialogEvent> dialogExecutor = new DefaultDialogExecutor<TestUtils.States, DialogEvent>(stateMachineService);

        dialogExecutor.applyEvent(sessionId, new DialogEvent("1"));

        // when
        DialogEventResult result = dialogExecutor.applyEvent(sessionId, new DialogEvent("2"));

        // then
        assertTrue(result.isDialogComplete());
        inOrder.verify(cachePersist).write(any(), eq(sessionId));
        inOrder.verify(cachePersist).read(sessionId);
        inOrder.verify(cachePersist).write(any(), eq(sessionId));
        inOrder.verify(cachePersist).delete(sessionId);
    }

    @Test
    public void itemHandlerExecutedBeforeTransitionAction() throws Exception {
        // given
        String VARIABLE_KEY = "foo";
        String VARIABLE_VALUE = "bar";
        AtomicBoolean variableSet = new AtomicBoolean(false);

        Action<TestUtils.States, DialogEvent> transitionAction = (context) -> {
            // expecting this value to have been set by the OnItemHandler below
            String value = context.getExtendedState().get(VARIABLE_KEY, String.class);
            variableSet.set(VARIABLE_VALUE.equals(value));
        };

        OnItemHandler handler = OnItemHandlers.setExtendedStateVariable(VARIABLE_KEY, VARIABLE_VALUE);

        StateMachineFactory<TestUtils.States, DialogEvent> stateMachineFactory = createStateMachineWithMenuScreenTransitions(transitionAction, handler);
        CachePersist<TestUtils.States, DialogEvent> cachePersist = createCachePersist();
        StateMachineService<TestUtils.States, DialogEvent> stateMachineService = new DefaultStateMachineService<>(stateMachineFactory, cachePersist, cachePersist);

        DialogExecutor<TestUtils.States, DialogEvent> dialogExecutor = new DefaultDialogExecutor<TestUtils.States, DialogEvent>(stateMachineService);

        // when
        DialogEventResult result = dialogExecutor.applyEvent("session222", new DialogEvent("1"));

        // then
        assertTrue(variableSet.get());
    }

    private StateMachineFactory<TestUtils.States, DialogEvent> createStateMachineThrowingException() throws Exception {
        DialogStateMachineFactoryBuilder.Builder<TestUtils.States, DialogEvent> builder = DialogStateMachineFactoryBuilder.builder(DialogEvent.class);

        @SuppressWarnings("unchecked")
        Action<TestUtils.States, DialogEvent> initialAction = Mockito.mock(Action.class);

        @SuppressWarnings("unchecked")
        Action<TestUtils.States, DialogEvent> bEntryAction = Mockito.mock(Action.class);
        Mockito.doThrow(new TestException()).when(bEntryAction).execute(any());

        builder.withState(TestUtils.createState(TestUtils.States.A, initialAction));
        builder.withState(TestUtils.createState(TestUtils.States.B, bEntryAction));
        builder.withInitialState(TestUtils.States.A);

        builder.withTransition(TestUtils.States.A, TestUtils.States.B, new TestUtils.AlwaysTrueGuard<TestUtils.States, DialogEvent>());

        return builder.build();
    }

    private StateMachineFactory<TestUtils.States, DialogEvent> createStateMachineToEndState() throws Exception {
        DialogStateMachineFactoryBuilder.Builder<TestUtils.States, DialogEvent> builder = DialogStateMachineFactoryBuilder.builder(DialogEvent.class);

        builder.withState(TestUtils.createState(TestUtils.States.A, new TestUtils.NopAction<>()));
        builder.withState(TestUtils.createState(TestUtils.States.B, new TestUtils.NopAction<>()));
        builder.withState(TestUtils.createEndState(TestUtils.States.C, new TestUtils.NopAction<>()));
        builder.withInitialState(TestUtils.States.A);

        builder.withTransition(TestUtils.States.A, TestUtils.States.B, new RegExpTransitionGuard<>(Pattern.compile("1")));
        builder.withTransition(TestUtils.States.B, TestUtils.States.C, new RegExpTransitionGuard<>(Pattern.compile("2")));

        return builder.build();
    }

    private StateMachineFactory<TestUtils.States, DialogEvent> createStateMachineWithMenuScreenTransitions(Action<TestUtils.States, DialogEvent> transitionAction, OnItemHandler onItemHandler) throws Exception {
        DialogStateMachineFactoryBuilder.Builder<TestUtils.States, DialogEvent> builder = DialogStateMachineFactoryBuilder.builder(DialogEvent.class);

        TestUtils.Transitions transition = TestUtils.Transitions.T1;
        Action<TestUtils.States, DialogEvent> stateAEntryAction = new Action<TestUtils.States, DialogEvent>() {
            @Override
            public void execute(StateContext<TestUtils.States, DialogEvent> context) {
                MenuItem mi = new MenuItem("Option 1", transition, onItemHandler);

                Screen.Builder screenBuilder =  Screen.builder();
                MenuGroup.Builder mgBuilder = MenuGroup.builder();
                mgBuilder.withMenuItem(mi);
                screenBuilder.withScreenBlock(mgBuilder.build());
                Screen screen = screenBuilder.build();
                ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
            }
        };

        // should be able to only have state A and B but the entry action is not getting executed for the initial state
        builder.withState(TestUtils.createState(TestUtils.States.A, stateAEntryAction));
        builder.withState(TestUtils.createState(TestUtils.States.B, new TestUtils.NopAction<>()));
        builder.withInitialState(TestUtils.States.A);

        builder.withScreenTransition(TestUtils.States.A, TestUtils.States.B, transition, transitionAction);
        return builder.build();
    }


    private CachePersist<TestUtils.States, DialogEvent> createCachePersist() {
        return new CachePersist<>(new ConcurrentMapCache("cache"));
    }


    private static class TestException extends RuntimeException {

        private static final long serialVersionUID = -8536421441303666632L;
    }

}
