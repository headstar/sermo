package demo.bank;

import com.headstartech.sermo.*;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) throws Exception {

        StateMachineFactoryBuilder.Builder<States, SubscriberEvent> stateMachineFactoryBuilder = StateMachineFactoryBuilder.builder();
        USSDAppSupport.Builder<States, SubscriberEvent> builder = USSDAppSupport.builder(stateMachineFactoryBuilder.configureStates().withStates(), stateMachineFactoryBuilder.configureTransitions(),
                SubscriberEvent.class);

        USSDState<States, SubscriberEvent> rootMenuScreen = new USSDState<>(States.ROOT, new RootEntryAction());
        USSDState<States, SubscriberEvent> accountsScreen = new PagedUSSDState<>(States.ACCOUNTS, new AccountsEntryAction());
        USSDState<States, SubscriberEvent> statementScreen = new USSDState<>(States.STATEMENT, new StatementEntryAction());
        USSDState<States, SubscriberEvent> accountDetailsScreen = new USSDState<>(States.ACCOUNT_DETAILS, new AccountDetailStateEntryAction());

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

        USSDApplication<States, SubscriberEvent> ussdApplication = new USSDApplication(stateMachineFactoryBuilder.build().getStateMachine());
        ussdApplication.start();
        System.out.println("started\n");

        String msisdn = "888888";

        String result = ussdApplication.applyEvent(new SubscriberEvent("111", msisdn));
        System.out.println(result);

        result = ussdApplication.applyEvent(new SubscriberEvent("1", msisdn));
        System.out.println(result);

        result = ussdApplication.applyEvent(new SubscriberEvent("0", msisdn));
        System.out.println(result);

        result = ussdApplication.applyEvent(new SubscriberEvent("0", msisdn));
        System.out.println(result);

		result = ussdApplication.applyEvent(new SubscriberEvent("#", msisdn));
		System.out.println(result);

		result = ussdApplication.applyEvent(new SubscriberEvent("1", msisdn));
		System.out.println(result);

		result = ussdApplication.applyEvent(new SubscriberEvent("1", msisdn));
		System.out.println(result);

	}

    private static class Listener extends StateMachineListenerAdapter<States, SubscriberEvent> {

        @Override
        public void eventNotAccepted(Message<SubscriberEvent> event) {
            System.out.println("Event not accepted " + event);
        }

        @Override
        public void stateMachineError(StateMachine<States, SubscriberEvent> stateMachine, Exception exception) {
            System.out.println("Machine error");
        }
    }

}

