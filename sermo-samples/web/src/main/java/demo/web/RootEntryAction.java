package demo.web;

import com.headstartech.sermo.*;
import com.headstartech.sermo.screen.MenuGroup;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.Text;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class RootEntryAction extends MenuScreenEntryAction<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Main Menu"));

        MenuGroup accounts = MenuGroup.builder()
                .withMenuItem("Accounts", Transitions.ACCOUNTS)
                .withMenuItem("Statements", Transitions.STATEMENT)
                .withMenuItem("Exit", Transitions.EXIT)
                .build();
        screenBuilder.withScreenBlock(accounts);

        Screen screen = screenBuilder.build();
        setScreenMenu(context.getExtendedState(), screen);
    }

}
