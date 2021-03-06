package demo.web.dialog;

import com.headstartech.sermo.DialogExecutor;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.factory.ChoiceOption;
import com.headstartech.sermo.statemachine.factory.DialogStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import com.headstartech.sermo.states.USSDState;
import com.headstartech.sermo.states.USSDStates;
import com.headstartech.sermo.support.DefaultDialogExecutor;
import com.headstartech.sermo.support.MDCDialogListener;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
public class DialogConfiguration {

    private static final String mainMenuShortCode = "*8444#";
    private static final String statementShortCode = "*8444*11#";

    @Bean
    public CachePersist<States, SubscriberEvent> stateMachinePersist() {
        return new CachePersist<>(new ConcurrentMapCache("bank"));
    }

    @Bean
    public USSDState<States, SubscriberEvent> rootMenu() {
        return USSDStates.menuState(States.ROOT, new RootEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> accountsMenu() {
        return USSDStates.pagedScreenState(States.ACCOUNTS, new PagedAccountsSetupProvider());
    }

    @Bean
    public USSDState<States, SubscriberEvent> statementsMenu() {
        return USSDStates.menuState(States.STATEMENT, new StatementEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> statementsMonthlyMenu() {
        return USSDStates.menuState(States.STATEMENT_MONTHLY, new StatementMonthlyEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> statementsAnnualMenu() {
        return USSDStates.menuState(States.STATEMENT_ANNUAL, new StatementAnnualEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> accountsDetailMenu() {
        return USSDStates.menuState(States.ACCOUNT_DETAILS, new AccountDetailStateEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> endMenu() {
        return USSDStates.endState(States.END);
    }

    @Bean
    public USSDState<States, SubscriberEvent> interestMenu() {
        return USSDStates.menuState(States.INTEREST_RATE, new InterestEntryAction());
    }

    @Bean
    public USSDState<States, SubscriberEvent> interestRateOfffeMenu() {
        return USSDStates.menuState(States.INTEREST_RATE_OFFER, new InterestOfferEntryAction());
    }


    @Bean
    public DialogExecutor<States, SubscriberEvent> dialogExecutor(CachePersist<States, SubscriberEvent> cachePersist,
                                                                  Collection<USSDState<States, SubscriberEvent>> states) throws Exception {

        DialogStateMachineFactoryBuilder.Builder<States, SubscriberEvent> builder = DialogStateMachineFactoryBuilder.builder(SubscriberEvent.class);

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
        builder.withScreenTransition(States.INTEREST_RATE, States.ROOT, Transitions.ROOT);


        builder.withChoice(States.STATEMENT_CHOICE, States.STATEMENT_ANNUAL, new ChoiceOption<>(States.STATEMENT_MONTHLY, (e) -> false));

        builder.withFormInputTransitions(States.INTEREST_RATE, States.INTEREST_RATE_OFFER, new OverEighteenGuard());
        builder.withScreenTransition(States.INTEREST_RATE_OFFER, States.ROOT, Transitions.ROOT);

        DialogExecutor<States, SubscriberEvent> dialogExecutor = new DefaultDialogExecutor<>(builder.build(), cachePersist);
        dialogExecutor.addListener(new MDCDialogListener<>());
        return dialogExecutor;
    }

    @Bean
    public List<ShortCode> shortCodes() {
        List<ShortCode> shortCodes = new ArrayList<>();

        shortCodes.add(new ShortCode(mainMenuShortCode, "Main menu"));
        shortCodes.add(new ShortCode(statementShortCode, "Statement"));

        return shortCodes;
    }

}
