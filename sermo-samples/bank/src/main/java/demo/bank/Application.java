package demo.bank;

import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.USSDApplication;
import com.headstartech.sermo.USSDApplicationBuilder;
import com.headstartech.sermo.USSDState;

import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) throws Exception {
        USSDApplicationBuilder builder = USSDApplicationBuilder.builder();
        USSDState rootMenu = new RootMenuUSSDState();
        USSDState accountsMenu = new AccountsMenu();
        USSDState statementMenu = new StatementMenuUSSDState();
        USSDState accountDetailState = new AccountDetailState();

        builder.withState(rootMenu);
        builder.withState(accountsMenu);
        builder.withState(statementMenu);
        builder.withState(accountDetailState);

        builder.withInitialTransition(rootMenu, Pattern.compile("111"));
        builder.withInitialTransition(statementMenu, Pattern.compile("222"));

        builder.withMenuTransition(rootMenu, accountsMenu, RootMenuItems.ACCOUNTS);
        builder.withMenuTransition(accountsMenu, accountDetailState, RootMenuItems.ACCOUNT_DETAIL);
        builder.withMenuTransition(rootMenu, statementMenu, RootMenuItems.STATEMENT);

        USSDApplication ussdApplication = builder.build();

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


}

