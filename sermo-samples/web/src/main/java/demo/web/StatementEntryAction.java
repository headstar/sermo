package demo.web;

import com.headstartech.sermo.SubscriberEvent;
import com.headstartech.sermo.screen.*;
import com.headstartech.sermo.support.ExtendedStateSupport;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * @author Per Johansson
 */
public class StatementEntryAction implements Action<States, SubscriberEvent> {

    @Override
    public void execute(StateContext<States, SubscriberEvent> context) {
        Screen.Builder screenBuilder =  Screen.builder();

        screenBuilder.withScreenBlock(new Text("Statement"));
        screenBuilder.withScreenBlock(EmptyLine.INSTANCE);

        MenuGroup statements = MenuGroup.builder()
                .withMenuItem("Detail", Transitions.STATEMENT_CHOICE).build();

        screenBuilder.withScreenBlock(statements);

        screenBuilder.withScreenBlock(new StaticMenuItem("#", "Main menu", Transitions.ROOT));

        Screen screen = screenBuilder.build();
        ExtendedStateSupport.setScreen(context.getExtendedState(), screen);
    }
}
