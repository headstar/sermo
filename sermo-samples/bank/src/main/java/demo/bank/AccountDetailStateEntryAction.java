package demo.bank;

import com.headstartech.sermo.ExtendedStateSupport;
import com.headstartech.sermo.SubscriberEvent;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class AccountDetailStateEntryAction implements Action<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        System.out.println("item key " + ExtendedStateSupport.getItemData(context.getExtendedState()));
    }

}
