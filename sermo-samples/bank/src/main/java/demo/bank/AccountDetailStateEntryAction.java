package demo.bank;

import com.headstartech.sermo.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class AccountDetailStateEntryAction implements Action<States, Object> {

    @Override
    public void execute(StateContext<States, Object> context) {
        System.out.println("item key " + ExtendedStateSupport.getItemData(context.getExtendedState()));
    }

}
