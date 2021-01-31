package demo.web.dialog;

import com.headstartech.sermo.screen.MenuGroup;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.Text;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class RootEntryAction implements Action<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Main Menu"));

        MenuGroup accounts = MenuGroup.builder()
                .withMenuItem("Accounts", Transitions.ACCOUNTS)
                .withMenuItem("Statements", Transitions.STATEMENT)
                .withMenuItem("Interest rates", Transitions.INTEREST)
                .withMenuItem("Exit", Transitions.EXIT)
                .build();
        screenBuilder.withScreenBlock(accounts);

        Screen screen = screenBuilder.build();
        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }

}
