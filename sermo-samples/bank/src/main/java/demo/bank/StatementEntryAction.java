package demo.bank;

import com.headstartech.sermo.support.ExtendedStateSupport;
import com.headstartech.sermo.SubscriberEvent;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class StatementEntryAction implements Action<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        ExtendedStateSupport.setOutput(context.getExtendedState(), "Statement...");
    }
}
