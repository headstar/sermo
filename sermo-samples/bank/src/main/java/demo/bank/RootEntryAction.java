package demo.bank;

import com.headstartech.sermo.*;
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
                .build();
        screenBuilder.withScreenBlock(accounts);

        Screen screen = screenBuilder.build();

        ExtendedStateSupport.setScreenMenuInputMap(context.getExtendedState(), screen.getInputMap());
        ExtendedStateSupport.setOutput(context.getExtendedState(), screen.getOutput());
    }

}
