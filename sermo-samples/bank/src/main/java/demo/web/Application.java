package demo.web;

import com.headstartech.sermo.SermoDialogExecutor;
import com.headstartech.sermo.SermoMetricsConfiguration;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.factory.ChoiceOption;
import com.headstartech.sermo.statemachine.factory.SermoStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import com.headstartech.sermo.states.PagedUSSDState;
import com.headstartech.sermo.states.USSDEndState;
import com.headstartech.sermo.states.USSDState;
import com.headstartech.sermo.support.MDCSermoDialogListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootApplication
@Import(SermoMetricsConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static final String mainMenuShortCode = "*8444#";
    private static final String statementShortCode = "*8444*11#";

    @Bean
    public CachePersist<States, SubscriberEvent> stateMachinePersist() {
        return new CachePersist<>(new ConcurrentMapCache("bank"));
    }

    @Bean
    public USSDState<States, SubscriberEvent> rootMenu() {
        return new USSDState<>(States.ROOT, new RootEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> accountsMenu() {
        return new PagedUSSDState<>(States.ACCOUNTS, new AccountsEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> statementsMenu() {
        return new USSDState<>(States.STATEMENT, new StatementEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> statementsMonthlyMenu() {
        return new USSDState<>(States.STATEMENT_MONTHLY, new StatementMonthlyEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> statementsAnnualMenu() {
        return new USSDState<>(States.STATEMENT_ANNUAL, new StatementAnnualEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> accountsDetailMenu() {
        return new USSDState<>(States.ACCOUNT_DETAILS, new AccountDetailStateEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> endMenu() {
        return new USSDEndState<>(States.END);
    }

    @Bean
    public SermoDialogExecutor<States, SubscriberEvent> dialogExecutor(CachePersist<States, SubscriberEvent> cachePersist,
                                                                       Collection<USSDState<States, SubscriberEvent>> states) throws Exception {

        SermoStateMachineFactoryBuilder.Builder<States, SubscriberEvent> builder = SermoStateMachineFactoryBuilder.builder(SubscriberEvent.class);
        
        builder.withStates(states);

        builder.withInitialState(States.INITIAL);
        builder.withInitialTransition(States.ROOT, new RegExpTransitionGuard<>(Pattern.compile(Pattern.quote(mainMenuShortCode))));
        builder.withInitialTransition(States.STATEMENT, new RegExpTransitionGuard<>(Pattern.compile(Pattern.quote(statementShortCode))));

        builder.withLoggingListener();

        builder.withScreenTransition(States.ROOT, States.ACCOUNTS, Transitions.ACCOUNTS);
        builder.withScreenTransition(States.ACCOUNTS, States.ACCOUNT_DETAILS, Transitions.ACCOUNT_DETAIL);
        builder.withScreenTransition(States.ROOT, States.STATEMENT, Transitions.STATEMENT);
        builder.withScreenTransition(States.ROOT, States.END, Transitions.EXIT);
        builder.withScreenTransition(States.ACCOUNT_DETAILS, States.ROOT, Transitions.ROOT);
        builder.withScreenTransition(States.STATEMENT, States.ROOT, Transitions.ROOT);
        builder.withScreenTransition(States.STATEMENT, States.STATEMENT_CHOICE, Transitions.STATEMENT_CHOICE);
        builder.withScreenTransition(States.STATEMENT, States.ROOT, Transitions.ROOT);
        builder.withScreenTransition(States.STATEMENT_MONTHLY, States.ROOT, Transitions.ROOT);
        builder.withScreenTransition(States.STATEMENT_ANNUAL, States.ROOT, Transitions.ROOT);


        builder.withChoice(States.STATEMENT_CHOICE, States.STATEMENT_ANNUAL, Arrays.asList(new ChoiceOption<>(States.STATEMENT_MONTHLY, (e) -> false)));

        SermoDialogExecutor<States, SubscriberEvent> sermoDialogExecutor = new SermoDialogExecutor<>(builder.build(), cachePersist);
        sermoDialogExecutor.register(new MDCSermoDialogListener<>());
        return sermoDialogExecutor;
    }

    @Bean
    public List<ShortCode> shortCodes() {
        List<ShortCode> shortCodes = new ArrayList<>();

        shortCodes.add(new ShortCode(mainMenuShortCode, "Main menu"));
        shortCodes.add(new ShortCode(statementShortCode, "Statement"));

        return shortCodes;
    }

}