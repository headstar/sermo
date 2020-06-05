package demo.bank;

import com.headstartech.sermo.DialogEventResult;
import com.headstartech.sermo.SermoDialogExecutor;
import com.headstartech.sermo.SubscriberEvent;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.DefaultStateMachinePool;
import com.headstartech.sermo.statemachine.factory.SermoStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.factory.StateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import com.headstartech.sermo.states.PagedUSSDState;
import com.headstartech.sermo.states.USSDEndState;
import com.headstartech.sermo.states.USSDState;
import com.headstartech.sermo.support.DefaultSermoStateMachineService;
import com.headstartech.sermo.support.SermoStateMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {

        StateMachineFactoryBuilder.Builder<States, SubscriberEvent> stateMachineFactoryBuilder = StateMachineFactoryBuilder.builder();
        SermoStateMachineFactoryBuilder.Builder<States, SubscriberEvent> builder = SermoStateMachineFactoryBuilder.builder(stateMachineFactoryBuilder, SubscriberEvent.class);

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
                .withState(endScreen);

        builder.withInitialState(States.INITIAL);
        builder.withInitialTransition(States.ROOT, new RegExpTransitionGuard<>(Pattern.compile("111")));
        builder.withInitialTransition(States.STATEMENT, new RegExpTransitionGuard<>(Pattern.compile("222")));

        builder.withScreenTransition(States.ROOT, States.ACCOUNTS, Transitions.ACCOUNTS);
        builder.withScreenTransition(States.ACCOUNTS, States.ACCOUNT_DETAILS, Transitions.ACCOUNT_DETAIL);
        builder.withScreenTransition(States.ROOT, States.STATEMENT, Transitions.STATEMENT);
        builder.withScreenTransition(States.ROOT, States.END, Transitions.EXIT);

        CachePersist<States, SubscriberEvent> stateMachinePersist = new CachePersist<>(new ConcurrentMapCache("bank"));
        SermoStateMachineService<States, SubscriberEvent> sermoStateMachineService = new DefaultSermoStateMachineService<>(new DefaultStateMachinePool<>(stateMachineFactoryBuilder.build()),
                new DefaultStateMachinePersister<>(stateMachinePersist), stateMachinePersist);

        SermoDialogExecutor<States, SubscriberEvent> sermoDialogExecutor = new SermoDialogExecutor<>(sermoStateMachineService);

        String msisdn = "888888";

        List<String> inputs = Arrays.asList("111", "1", "0", "0", "#", "1", "1");
        try {
            for (int i = 0; i < inputs.size(); ++i) {
                DialogEventResult result = sermoDialogExecutor.applyEvent(msisdn, new SubscriberEvent(inputs.get(i), msisdn));
                System.out.println(result.getOutput());
                if(result.isDialogComplete()) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("An internal error occured.\nPlease try again later!");
        }
	}

}

