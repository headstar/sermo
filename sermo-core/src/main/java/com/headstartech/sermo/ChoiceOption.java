package com.headstartech.sermo;

import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.guard.Guard;

/**
 * @author Per Johansson
 */
public class ChoiceOption<S, E> {

    private final S target;
    private final Guard<S, E> guard;
    private final Action<S, E> action;

    public ChoiceOption(S target, Guard<S, E> guard) {
        this(target, guard, null);
    }

    public ChoiceOption(S target, Guard<S, E> guard, Action<S, E> action) {
        this.target = target;
        this.guard = guard;
        this.action = action;
    }

    public S getTarget() {
        return target;
    }

    public Guard<S, E> getGuard() {
        return guard;
    }

    public Action<S, E> getAction() {
        return action;
    }
}
