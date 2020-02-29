package demo.web;

import com.headstartech.sermo.SubscriberEvent;
import com.headstartech.sermo.actions.MenuScreenEntryAction;
import com.headstartech.sermo.screen.EmptyLine;
import com.headstartech.sermo.screen.Screen;
import com.headstartech.sermo.screen.StaticMenuItem;
import com.headstartech.sermo.screen.Text;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class StatementAnnualEntryAction extends MenuScreenEntryAction<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Statement Annual"));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);

        screenBuilder.withScreenBlock(new StaticMenuItem("#", "Main menu", Transitions.ROOT));

        Screen screen = screenBuilder.build();
        setScreen(context.getExtendedState(), screen);
    }
}
