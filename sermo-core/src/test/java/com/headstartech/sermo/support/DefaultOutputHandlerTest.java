package com.headstartech.sermo.support;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.SystemConstants;
import com.headstartech.sermo.TestUtils;
import com.headstartech.sermo.statemachine.factory.DialogStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateMachine;

import java.util.regex.Pattern;

import static com.headstartech.sermo.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultOutputHandlerTest {

    @Test
    public void returnsEventOutputSetLastOutput() throws Exception {
        // given
        OutputHandler<States, DialogEvent> outputHandler = new DefaultOutputHandler<>();
        String expectedOutput = "ABC";
        StateMachine<States, DialogEvent> stateMachine = getStateMachine();
        stateMachine.getExtendedState().getVariables().put(SystemConstants.OUTPUT_KEY, expectedOutput);
        assertFalse(stateMachine.isComplete());

        // when
        String actualOutput = outputHandler.getOutput(stateMachine);

        // then
        assertEquals(expectedOutput, actualOutput);
        assertNull(stateMachine.getExtendedState().getVariables().get(SystemConstants.OUTPUT_KEY));
        assertEquals(expectedOutput, stateMachine.getExtendedState().getVariables().get(SystemConstants.LAST_OUTPUT_KEY));
    }

    @Test
    public void returnsLastOutputWhenEventOutputIsNull() throws Exception {
        // given
        OutputHandler<States, DialogEvent> outputHandler = new DefaultOutputHandler<>();
        StateMachine<States, DialogEvent> stateMachine = getStateMachine();
        assertFalse(stateMachine.isComplete());
        String expectedOutput = "ABC";
        stateMachine.getExtendedState().getVariables().put(SystemConstants.LAST_OUTPUT_KEY, expectedOutput);

        // when
        String actualOutput = outputHandler.getOutput(stateMachine);

        // then
        assertEquals(expectedOutput, actualOutput);
        assertEquals(expectedOutput, stateMachine.getExtendedState().getVariables().get(SystemConstants.LAST_OUTPUT_KEY));
        assertNull(stateMachine.getExtendedState().getVariables().get(SystemConstants.OUTPUT_KEY));
    }

    @Test
    public void returnsNullIfStateMachineCompletedAndEventOutputIsNull() throws Exception {
        // given
        OutputHandler<States, DialogEvent> outputHandler = new DefaultOutputHandler<>();
        StateMachine<States, DialogEvent> stateMachine = getCompletedStateMachine();
        assertTrue(stateMachine.isComplete());
        stateMachine.getExtendedState().getVariables().put(SystemConstants.LAST_OUTPUT_KEY, "ABCDEF");

        // when
        String actualOutput = outputHandler.getOutput(stateMachine);

        // then
        assertNull(actualOutput);
    }

    private StateMachine<States, DialogEvent> getStateMachine() throws Exception {
        DialogStateMachineFactoryBuilder.Builder<TestUtils.States, DialogEvent> builder = DialogStateMachineFactoryBuilder.builder(DialogEvent.class);
        builder.withState(createState(TestUtils.States.A, nopAction()));
        builder.withState(createEndState(TestUtils.States.B, nopAction()));
        builder.withInitialState(TestUtils.States.A);
        builder.withTransition(TestUtils.States.A, TestUtils.States.B, new RegExpTransitionGuard<>(Pattern.compile("1")));
        StateMachine<States, DialogEvent> stateMachine = builder.build().getStateMachine();
        stateMachine.start();
        return stateMachine;
    }

    private StateMachine<States, DialogEvent> getCompletedStateMachine() throws Exception {
        StateMachine<States, DialogEvent> stateMachine = getStateMachine();
        stateMachine.start();
        stateMachine.sendEvent(new DialogEvent("1"));
        assertTrue(stateMachine.isComplete());
        return stateMachine;
    }
}
