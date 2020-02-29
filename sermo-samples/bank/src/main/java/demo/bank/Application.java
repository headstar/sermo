package demo.bank;

import com.headstartech.sermo.*;
import com.headstartech.sermo.actions.SetFixedOutputOnError;
import com.headstartech.sermo.states.PagedUSSDState;
import com.headstartech.sermo.states.USSDEndState;
import com.headstartech.sermo.states.USSDState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {

        StateMachineFactoryBuilder.Builder<States, SubscriberEvent> stateMachineFactoryBuilder = StateMachineFactoryBuilder.builder();
        USSDApplicationBuilder.Builder<States, SubscriberEvent> builder = USSDApplicationBuilder.builder(stateMachineFactoryBuilder, SubscriberEvent.class);

        USSDState<States, SubscriberEvent> rootMenuScreen = new USSDState<>(States.ROOT, new RootEntryAction());
        USSDState<States, SubscriberEvent> accountsScreen = new PagedUSSDState<>(States.ACCOUNTS, new AccountsEntryAction());
        USSDState<States, SubscriberEvent> statementScreen = new USSDState<>(States.STATEMENT, new StatementEntryAction());
        USSDState<States, SubscriberEvent> accountDetailsScreen = new USSDState<>(States.ACCOUNT_DETAILS, new AccountDetailStateEntryAction());
        USSDEndState<States, SubscriberEvent> endScreen = new USSDEndState<>(States.END);

        builder
                .withState(rootMenuScreen)
                .withState(accountsScreen)
                .withState(statementScreen)
                .withState(accountDetailsScreen)
                .withEndState(endScreen);

        builder.withInitialState(States.INITIAL);
        builder.withShortCodeTransition(States.ROOT, Pattern.compile("111"));
        builder.withShortCodeTransition(States.STATEMENT, Pattern.compile("222"));

        builder.withScreenTransition(States.ROOT, States.ACCOUNTS, Transitions.ACCOUNTS);
        builder.withScreenTransition(States.ACCOUNTS, States.ACCOUNT_DETAILS, Transitions.ACCOUNT_DETAIL);
        builder.withScreenTransition(States.ROOT, States.STATEMENT, Transitions.STATEMENT);
        builder.withScreenTransition(States.ROOT, States.END, Transitions.EXIT);

        builder.withErrorAction(new SetFixedOutputOnError<>("An internal error occured.\nPlease try again later!"));

        ExtendedStateMachinePersist<States, SubscriberEvent, String> stateMachinePersist = new InMemoryStateMachinePersist<>();
        USSDStateMachineService<States, SubscriberEvent> ussdStateMachineService = new DefaultUssdStateMachineService<>(new DefaultStateMachinePool<>(stateMachineFactoryBuilder.build()),
                new DefaultExtendedStateMachinePersister<>(stateMachinePersist));

        USSDApplication<States, SubscriberEvent> ussdApplication = new USSDApplication<>(ussdStateMachineService);

        String msisdn = "888888";

        List<String> inputs = Arrays.asList("111", "1", "0", "0", "#", "1", "1");
        for(int i=0; i<inputs.size(); ++i) {
            EventResult result = ussdApplication.applyEvent(msisdn, new SubscriberEvent(inputs.get(i), msisdn));
            if(result.isApplicationError()) {
                System.out.println("Internal error\n" + result.getOutput().orElse(""));
                break;
            } else {
                System.out.println(result.getOutput());
            }
            if(result.isApplicationCompleted()) {
                break;
            }
        }
	}

}

