package demo.web;

import com.headstartech.sermo.screen.EmptyLine;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.StaticMenuItem;
import com.headstartech.sermo.screen.Text;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class AccountDetailStateEntryAction implements Action<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        AccountDetailsDTO accountDetailsDTO = (AccountDetailsDTO) context.getExtendedState().getVariables().remove(Constants.ACCOUNT_DATA_KEY);

        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Account"));
        screenBuilder.withScreenBlock(new Text(accountDetailsDTO.getAccountId()));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);
        screenBuilder.withScreenBlock(new Text("Balance"));
        screenBuilder.withScreenBlock(new Text(String.format("%d EUR", accountDetailsDTO.getAmount())));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);
        screenBuilder.withScreenBlock(new StaticMenuItem("#", "Main menu", Transitions.ROOT));

        Screen screen = screenBuilder.build();
        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }

}
