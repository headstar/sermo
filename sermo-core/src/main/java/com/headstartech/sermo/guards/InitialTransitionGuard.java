package com.headstartech.sermo.guards;

import com.headstartech.sermo.MOInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Per Johansson
 */
public class InitialTransitionGuard<S, E extends MOInput> extends GuardBase<S, E> {

    private static final Logger log = LoggerFactory.getLogger(InitialTransitionGuard.class);

    private final Pattern pattern;

    public InitialTransitionGuard(Pattern pattern) {
        this.pattern = pattern;
    }

    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean res = false;
        if(input != null) {
            Matcher m = pattern.matcher(input);
            res = m.matches();
        }
        log.debug("Initial transition guard evaluation: result={}, input={}, pattern={}", res, input, pattern.pattern());
        return res;
    }

}
