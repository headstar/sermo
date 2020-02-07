package demo.bank;

import com.headstartech.sermo.*;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.function.Function;
import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) throws Exception {

        StateMachineFactoryBuilder.Builder<States, Object> stateMachineFactoryBuilder = StateMachineFactoryBuilder.builder();
        USSDAppSupport.Builder builder = USSDAppSupport.builder(stateMachineFactoryBuilder.configureStates().withStates(), stateMachineFactoryBuilder.configureTransitions(),
                new EventToInput(), MOInput.INSTANCE);

        USSDState<States, Object> rootMenuScreen = new USSDState<>(States.ROOT, new RootEntryAction());
        USSDState<States, Object> accountsScreen = new PagedUSSDState<>(States.ACCOUNTS, new AccountsEntryAction());
        USSDState<States, Object> statementScreen = new USSDState<>(States.STATEMENT, new StatementEntryAction());
        USSDState<States, Object> accountDetailsScreen = new USSDState<>(States.ACCOUNT_DETAILS, new AccountDetailStateEntryAction());

        builder
                .withState(rootMenuScreen)
                .withState(accountsScreen)
                .withState(statementScreen)
                .withState(accountDetailsScreen);

        builder.withInitialState(States.INITIAL);
        builder.withShortCodeTransition(States.ROOT, Pattern.compile("111"));
        builder.withShortCodeTransition(States.STATEMENT, Pattern.compile("222"));

        builder.withScreenTransition(States.ROOT, States.ACCOUNTS, Transitions.ACCOUNTS);
        builder.withScreenTransition(States.ACCOUNTS, States.ACCOUNT_DETAILS, Transitions.ACCOUNT_DETAIL);
        builder.withScreenTransition(States.ROOT, States.STATEMENT, Transitions.STATEMENT);

        stateMachineFactoryBuilder.configureConfiguration().withConfiguration().listener(new Listener());

        USSDApplication ussdApplication = new USSDApplication(stateMachineFactoryBuilder.build().getStateMachine());
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

	static class EventToInput implements Function<Object, String> {

        @Override
        public String apply(Object o) {
            if(o instanceof MOInput) {
                return ((MOInput) o).getInput();
            }
            return null;
        }

    }

	private static class Listener extends StateMachineListenerAdapter<States, Object> {

        @Override
        public void eventNotAccepted(Message<Object> event) {
            System.out.println("Event not accepted " + event);
        }
    }

}

