package com.headstartech.sermo;

import org.springframework.statemachine.persist.DefaultStateMachinePersister;

/**
 * @author Per Johansson
 */
public class DefaultExtendedStateMachinePersister<S, E extends MOInput, String>  extends DefaultStateMachinePersister <S, E, String> implements ExtendedStateMachinePersister<S, E, String> {

    private final ExtendedStateMachinePersist<S, E, String> stateMachinePersist;

    public DefaultExtendedStateMachinePersister(ExtendedStateMachinePersist<S, E, String> stateMachinePersist) {
        super(stateMachinePersist);
        this.stateMachinePersist = stateMachinePersist;
    }

    @Override
    public void delete(String contextObj) throws Exception {
        stateMachinePersist.delete(contextObj);
    }

}
