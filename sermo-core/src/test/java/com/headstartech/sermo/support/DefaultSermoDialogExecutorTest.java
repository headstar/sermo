package com.headstartech.sermo.support;

import com.headstartech.sermo.*;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.factory.SermoStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class DefaultSermoDialogExecutorTest {

    @Test
    public void applyEventThrowsWhenActionThrows() throws Exception {
        // given
        StateMachineFactory<TestUtils.States, DialogEvent> stateMachineFactory = createStateMachineThrowingException();
        CachePersist<TestUtils.States, DialogEvent> cachePersist = Mockito.spy(createCachePersist());
        SermoStateMachineService<TestUtils.States, DialogEvent> sermoStateMachineService = new DefaultSermoStateMachineService<>(stateMachineFactory, cachePersist, cachePersist);

        SermoDialogExecutor<TestUtils.States, DialogEvent> dialogExecutor = new DefaultSermoDialogExecutor<TestUtils.States, DialogEvent>(sermoStateMachineService);

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
        StateMachineFactory<TestUtils.States, DialogEvent> stateMachineFactory = createStateMachineToEndState();
        CachePersist<TestUtils.States, DialogEvent> cachePersist = Mockito.spy(createCachePersist());
        InOrder inOrder = inOrder(cachePersist);

        SermoStateMachineService<TestUtils.States, DialogEvent> sermoStateMachineService = new DefaultSermoStateMachineService<>(stateMachineFactory, cachePersist, cachePersist);

        SermoDialogExecutor<TestUtils.States, DialogEvent> dialogExecutor = new DefaultSermoDialogExecutor<TestUtils.States, DialogEvent>(sermoStateMachineService);

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

    private StateMachineFactory<TestUtils.States, DialogEvent> createStateMachineThrowingException() throws Exception {
        SermoStateMachineFactoryBuilder.Builder<TestUtils.States, DialogEvent> builder = SermoStateMachineFactoryBuilder.builder(DialogEvent.class);

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
        SermoStateMachineFactoryBuilder.Builder<TestUtils.States, DialogEvent> builder = SermoStateMachineFactoryBuilder.builder(DialogEvent.class);

        builder.withState(TestUtils.createState(TestUtils.States.A, new TestUtils.NopAction<>()));
        builder.withState(TestUtils.createState(TestUtils.States.B, new TestUtils.NopAction<>()));
        builder.withState(TestUtils.createEndState(TestUtils.States.C, new TestUtils.NopAction<>()));
        builder.withInitialState(TestUtils.States.A);

        builder.withTransition(TestUtils.States.A, TestUtils.States.B, new RegExpTransitionGuard<>(Pattern.compile("1")));
        builder.withTransition(TestUtils.States.B, TestUtils.States.C, new RegExpTransitionGuard<>(Pattern.compile("2")));

        return builder.build();
    }


    private CachePersist<TestUtils.States, DialogEvent> createCachePersist() {
        return new CachePersist<>(new ConcurrentMapCache("cache"));
    }


    private static class TestException extends RuntimeException {

        private static final long serialVersionUID = -8536421441303666632L;
    }

}
