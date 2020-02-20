package demo.web;

import com.headstartech.sermo.*;
import com.headstartech.sermo.actions.MenuScreenEntryAction;
import com.headstartech.sermo.screen.EmptyLine;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.StaticMenuItem;
import com.headstartech.sermo.screen.Text;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class AccountDetailStateEntryAction extends MenuScreenEntryAction<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        AccountDetailsDTO accountDetailsDTO = (AccountDetailsDTO) ExtendedStateSupport.getItemData(context.getExtendedState());

        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Account"));
        screenBuilder.withScreenBlock(new Text(accountDetailsDTO.getAccountId()));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);
        screenBuilder.withScreenBlock(new Text("Balance"));
        screenBuilder.withScreenBlock(new Text(String.format("%d EUR", accountDetailsDTO.getAmount())));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);
        screenBuilder.withScreenBlock(new StaticMenuItem("#", "Main menu", Transitions.ROOT));

        Screen screen = screenBuilder.build();
        setScreenMenu(context.getExtendedState(), screen);
    }

}
