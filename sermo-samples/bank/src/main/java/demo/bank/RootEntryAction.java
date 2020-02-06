package demo.bank;

import com.headstartech.sermo.ExtendedStateSupport;
import com.headstartech.sermo.MenuGroup;
import com.headstartech.sermo.Screen;
import com.headstartech.sermo.Text;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class RootEntryAction implements Action<States, Object> {

    @Override
    public void execute(StateContext<States, Object> context) {
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
