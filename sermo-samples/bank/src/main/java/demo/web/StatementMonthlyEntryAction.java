package demo.web;

import com.headstartech.sermo.SubscriberEvent;
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
public class StatementMonthlyEntryAction implements Action<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Statement Monthly"));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);

        screenBuilder.withScreenBlock(new StaticMenuItem("#", "Main menu", Transitions.ROOT));

        Screen screen = screenBuilder.build();

        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }
}
