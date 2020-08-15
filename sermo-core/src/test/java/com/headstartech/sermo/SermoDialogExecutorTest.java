package com.headstartech.sermo;

import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.factory.SermoStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import com.headstartech.sermo.states.USSDState;
import com.headstartech.sermo.states.USSDStates;
import com.headstartech.sermo.support.DefaultSermoStateMachineService;
import com.headstartech.sermo.support.SermoStateMachineService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.guard.Guard;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class SermoDialogExecutorTest {

    @Test
    public void applyEventThrowsWhenActionThrows() throws Exception {
        // given
        StateMachineFactory<States, DialogEvent> stateMachineFactory = createStateMachineThrowingException();
        CachePersist<States, DialogEvent> cachePersist = Mockito.spy(createCachePersist());
        SermoStateMachineService<States, DialogEvent> sermoStateMachineService = new DefaultSermoStateMachineService<>(stateMachineFactory, cachePersist, cachePersist);

        SermoDialogExecutor<States, DialogEvent> dialogExecutor = new SermoDialogExecutor<States, DialogEvent>(sermoStateMachineService);

        // when
        try {
            dialogExecutor.applyEvent("session1", new DialogEvent("1"));
            fail("Expected exception");
        } catch(Exception e) {
            // then
            assertTrue(e instanceof SermoDialogException);
            assertTrue(e.getCause() instanceof TestException);

            verify(cachePersist).delete("session1");
        }
    }

    @Test
    public void contextDeletedWhenEndStateReached() throws Exception {
        // given
        String sessionId = "session1";
        StateMachineFactory<States, DialogEvent> stateMachineFactory = createStateMachineToEndState();
        CachePersist<States, DialogEvent> cachePersist = Mockito.spy(createCachePersist());
        InOrder inOrder = inOrder(cachePersist);

        SermoStateMachineService<States, DialogEvent> sermoStateMachineService = new DefaultSermoStateMachineService<>(stateMachineFactory, cachePersist, cachePersist);

        SermoDialogExecutor<States, DialogEvent> dialogExecutor = new SermoDialogExecutor<States, DialogEvent>(sermoStateMachineService);

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

    private StateMachineFactory<States, DialogEvent> createStateMachineThrowingException() throws Exception {
        SermoStateMachineFactoryBuilder.Builder<States, DialogEvent> builder = SermoStateMachineFactoryBuilder.builder(DialogEvent.class);

        @SuppressWarnings("unchecked")
        Action<States, DialogEvent> initialAction = Mockito.mock(Action.class);

        @SuppressWarnings("unchecked")
        Action<States, DialogEvent> bEntryAction = Mockito.mock(Action.class);
        Mockito.doThrow(new TestException()).when(bEntryAction).execute(any());

        builder.withState(createState(States.A, initialAction));
        builder.withState(createState(States.B, bEntryAction));
        builder.withInitialState(States.A);

        builder.withTransition(States.A, States.B, new AlwaysTrueGuard<States, DialogEvent>());

        return builder.build();
    }

    private StateMachineFactory<States, DialogEvent> createStateMachineToEndState() throws Exception {
        SermoStateMachineFactoryBuilder.Builder<States, DialogEvent> builder = SermoStateMachineFactoryBuilder.builder(DialogEvent.class);

        builder.withState(createState(States.A, new NopAction<>()));
        builder.withState(createState(States.B, new NopAction<>()));
        builder.withState(createEndState(States.C, new NopAction<>()));
        builder.withInitialState(States.A);

        builder.withTransition(States.A, States.B, new RegExpTransitionGuard<>(Pattern.compile("1")));
        builder.withTransition(States.B, States.C, new RegExpTransitionGuard<>(Pattern.compile("2")));

        return builder.build();
    }


    private CachePersist<States, DialogEvent> createCachePersist() {
        return new CachePersist<>(new ConcurrentMapCache("cache"));
    }

    private USSDState<States, DialogEvent> createState(States state, Action<States, DialogEvent> action) {
        return USSDStates.menuState(state, action);
    }


    private USSDState<States, DialogEvent> createEndState(States state, Action<States, DialogEvent> action) {
        return USSDStates.endState(state, action);
    }

    private static class TestException extends RuntimeException {

        private static final long serialVersionUID = -8536421441303666632L;
    }

    private static enum States {
        A, B, C
    }

    private static class AlwaysTrueGuard<S, E> implements Guard<S, E> {
        @Override
        public boolean evaluate(StateContext<S, E> context) {
            return true;
        }
    }

    private static class NopAction<S, E> implements Action<S, E> {
        @Override
        public void execute(StateContext<S, E> context) {
        }
    }

}
