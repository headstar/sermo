package demo.web;

import com.headstartech.sermo.*;
import com.headstartech.sermo.statemachine.actions.MenuScreenEntryAction;
import com.headstartech.sermo.screen.*;
import org.springframework.statemachine.StateContext;

/**
 * @author Per Johansson
 */
public class StatementEntryAction extends MenuScreenEntryAction<States, SubscriberEvent> {

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
        setScreen(context.getExtendedState(), screen);
    }
}
