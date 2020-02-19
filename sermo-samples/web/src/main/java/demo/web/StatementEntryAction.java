package demo.web;

import com.headstartech.sermo.*;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class StatementEntryAction extends MenuScreenEntryAction<States, SubscriberEvent>  {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Statement"));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);
        screenBuilder.withScreenBlock(new StaticMenuItem("#", "Main menu", Transitions.ROOT));

        Screen screen = screenBuilder.build();
        setScreenMenu(context.getExtendedState(), screen);
    }
}
