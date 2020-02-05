package demo.bank;

import com.headstartech.sermo.*;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) throws Exception {

        StateMachineFactoryBuilder.Builder<String, Object> builder = StateMachineFactoryBuilder.builder();

        builder.configureStates().withStates()
                .stateEntry(States.ROOT.name(), new RootEntryAction())
                .state(States.ACCOUNTS.name(), new AccountsEntryAction(), new MenuScreenExitAction())
                .stateEntry(States.STATEMENT.name(), new StatementEntryAction())
                .stateEntry(States.ACCOUNT_DETAILS.name(), new AccountDetailStateEntryAction());

        USSDAppSupport.init(builder.configureStates().withStates());
        USSDAppSupport.withShortCodeTransition(builder.configureTransitions(), States.ROOT.name(), Pattern.compile("111"));
        USSDAppSupport.withShortCodeTransition(builder.configureTransitions(), States.STATEMENT.name(), Pattern.compile("222"));

        USSDAppSupport.withScreenTransition(builder.configureTransitions(), States.ROOT.name(), States.ACCOUNTS.name(), RootMenuItems.ACCOUNTS);
        USSDAppSupport.withScreenTransition(builder.configureTransitions(), States.ACCOUNTS.name(), States.ACCOUNT_DETAILS.name(), RootMenuItems.ACCOUNT_DETAIL);
        USSDAppSupport.withScreenTransition(builder.configureTransitions(), States.ROOT.name(), States.STATEMENT.name(), RootMenuItems.STATEMENT);

        USSDAppSupport.withPagedScreenTransitions(builder.configureTransitions(), States.ACCOUNTS.name(), new PagedMenuScreenInternalAction());

        builder.configureConfiguration().withConfiguration().listener(new Listener());

        USSDApplication ussdApplication = new USSDApplication(builder.build().getStateMachine());
        ussdApplication.start();
        System.out.println("started\n");

        String result = ussdApplication.applyEvent(new MOInput("111"));
        System.out.println(result);

        result = ussdApplication.applyEvent(new MOInput("1"));
        System.out.println(result);

        result = ussdApplication.applyEvent(new MOInput("0"));
        System.out.println(result);

        result = ussdApplication.applyEvent(new MOInput("0"));
        System.out.println(result);

		result = ussdApplication.applyEvent(new MOInput("#"));
		System.out.println(result);

		result = ussdApplication.applyEvent(new MOInput("1"));
		System.out.println(result);

		result = ussdApplication.applyEvent(new MOInput("1"));
		System.out.println(result);

	}

	private static class Listener extends StateMachineListenerAdapter<String, Object> {

        @Override
        public void eventNotAccepted(Message<Object> event) {
            System.out.println("Event not accepted " + event);
        }
    }

}

