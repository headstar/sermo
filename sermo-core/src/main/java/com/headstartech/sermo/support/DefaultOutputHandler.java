package com.headstartech.sermo.support;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.DialogExecutor;
import com.headstartech.sermo.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;

/**
 * Default implementation of {@link OutputHandler}.
 *
 * Stores the last non-null output produced by the application. If the output for the current event is null it returns the last non-null output.
 * E.g. if a menu with 2 options is displayed and the user inputs "3" the same menu will be displayed again.
 *
 */
public class DefaultOutputHandler<S, E extends DialogEvent> implements OutputHandler<S, E> {

    private static final Logger log = LoggerFactory.getLogger(DefaultOutputHandler.class);

    @Override
    public String getOutput(StateMachine<S, E> stateMachine) {
        String output = getEventOutput(stateMachine);
        if (output != null) {
            setLastOutputValue(stateMachine, output);
        } else if(!stateMachine.isComplete()) {
            String lastOutput = getLastOutputValue(stateMachine);
            if (lastOutput != null) {
                log.debug("No output set for event, using last output: lastOutput=\n{}", lastOutput);
                output = lastOutput;
            }
        }
        removeEventOutput(stateMachine);
        return output;
    }

    protected String getEventOutput(StateMachine<S, E> stateMachine) {
        return stateMachine.getExtendedState().get(SystemConstants.OUTPUT_KEY, String.class);
    }

    protected void removeEventOutput(StateMachine<S, E> stateMachine) {
        stateMachine.getExtendedState().getVariables().remove(SystemConstants.OUTPUT_KEY);
    }

    protected void setLastOutputValue(StateMachine<S, E> stateMachine, String output) {
        stateMachine.getExtendedState().getVariables().put(SystemConstants.LAST_OUTPUT_KEY, output);
    }

    protected String getLastOutputValue(StateMachine<S, E> stateMachine) {
        return stateMachine.getExtendedState().get(SystemConstants.LAST_OUTPUT_KEY, String.class);
    }
}
