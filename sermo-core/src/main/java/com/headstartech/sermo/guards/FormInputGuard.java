package com.headstartech.sermo.guards;

import com.headstartech.sermo.MOInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;

import java.util.function.Predicate;

/**
 * @author Per Johansson
 */
public class FormInputGuard<S, E extends MOInput> extends GuardBase<S, E> {

    private static final Logger log = LoggerFactory.getLogger(FormInputGuard.class);

    private final Predicate<String> predicate;

    public FormInputGuard(Predicate<String> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected boolean doEvaluate(StateContext<S, E> context, String input) {
        boolean result = predicate.test(input);
        log.debug("Form input guard evaluation: result={}, input={}, predicate={}", result, input, predicate);
        return result;
    }


}
