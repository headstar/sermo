package com.headstartech.sermo;

import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.factory.SermoStateMachineFactoryBuilder;
import com.headstartech.sermo.states.USSDState;
import com.headstartech.sermo.support.DefaultSermoStateMachineService;
import com.headstartech.sermo.support.SermoStateMachineService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.guard.Guard;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class SermoDialogExecutorTest {

    @Test
    public void applyEventThrowsWhenActionThrows() throws Exception {
        // given
        StateMachineFactory<States, DialogEvent> stateMachineFactory = createStateMachine();
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

    private StateMachineFactory<States, DialogEvent> createStateMachine() throws Exception {
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

    private CachePersist<States, DialogEvent> createCachePersist() {
        return new CachePersist<>(new ConcurrentMapCache("cache"));
    }

    private USSDState<States, DialogEvent> createState(States state, Action<States, DialogEvent> action) {
        return new USSDState<>(state, action);
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

}
