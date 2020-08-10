package demo.web;

import com.headstartech.sermo.SermoDialogExecutor;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.factory.ChoiceOption;
import com.headstartech.sermo.statemachine.factory.SermoStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import com.headstartech.sermo.states.DefaultPagedUSSDState;
import com.headstartech.sermo.states.USSDEndState;
import com.headstartech.sermo.states.DefaultUSSDState;
import com.headstartech.sermo.support.MDCSermoDialogListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootApplication
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
    public DefaultUSSDState<States, SubscriberEvent> rootMenu() {
        return new DefaultUSSDState<>(States.ROOT, new RootEntryAction());
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> accountsMenu() {
        return new DefaultPagedUSSDState<>(States.ACCOUNTS, new AccountsEntryAction());
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> statementsMenu() {
        return new DefaultUSSDState<>(States.STATEMENT, new StatementEntryAction());
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> statementsMonthlyMenu() {
        return new DefaultUSSDState<>(States.STATEMENT_MONTHLY, new StatementMonthlyEntryAction());
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> statementsAnnualMenu() {
        return new DefaultUSSDState<>(States.STATEMENT_ANNUAL, new StatementAnnualEntryAction());
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> accountsDetailMenu() {
        return new DefaultUSSDState<>(States.ACCOUNT_DETAILS, new AccountDetailStateEntryAction());
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> endMenu() {
        return new USSDEndState<>(States.END);
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> interestMenu() {
        return new DefaultUSSDState<>(States.INTEREST_RATE, new InterestEntryAction());
    }

    @Bean
    public DefaultUSSDState<States, SubscriberEvent> interestRateOfffeMenu() {
        return new DefaultUSSDState<>(States.INTEREST_RATE_OFFER, new InterestOfferEntryAction());
    }


    @Bean
    public SermoDialogExecutor<States, SubscriberEvent> dialogExecutor(CachePersist<States, SubscriberEvent> cachePersist,
                                                                       Collection<DefaultUSSDState<States, SubscriberEvent>> states) throws Exception {

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
        builder.withScreenTransition(States.ROOT, States.INTEREST_RATE, Transitions.INTEREST);


        builder.withChoice(States.STATEMENT_CHOICE, States.STATEMENT_ANNUAL, Arrays.asList(new ChoiceOption<>(States.STATEMENT_MONTHLY, (e) -> false)));

        builder.withFormInputTransition(States.INTEREST_RATE, States.INTEREST_RATE_OFFER, new OverEighteenPredicate());
        builder.withScreenTransition(States.INTEREST_RATE_OFFER, States.ROOT, Transitions.ROOT);

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