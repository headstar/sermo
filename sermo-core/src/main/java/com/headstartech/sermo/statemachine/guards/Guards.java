package com.headstartech.sermo.statemachine.guards;

import com.headstartech.sermo.DialogEvent;
import org.springframework.statemachine.guard.Guard;

import java.util.Arrays;

/**
 * Convenience class for creating {@link Guard}s.
 *
 */
public class Guards {

    private Guards() {}

    @SafeVarargs
    @SuppressWarnings({"varargs"})
    public static <S, E extends DialogEvent> AndGuard<S, E> and(Guard<S, E>... guards) {
        return new AndGuard<>(Arrays.asList(guards));
    }

    @SafeVarargs
    @SuppressWarnings({"varargs"})
    public static <S, E extends DialogEvent> OrGuard<S, E> or(Guard<S, E>... guards) {
        return new OrGuard<>(Arrays.asList(guards));
    }

    public static <S, E extends DialogEvent> NotGuard<S, E> not(Guard<S, E>  guard) {
        return new NotGuard<>(guard);
    }

    public static <S, E extends DialogEvent> InputEqualsGuard<S, E> eq(String value) {
        return new InputEqualsGuard<>(value);
    }

}
