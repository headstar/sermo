package demo.bank;

import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.USSDApplication;
import com.headstartech.sermo.USSDApplicationBuilder;
import com.headstartech.sermo.USSDState;

public class Application {

    public static void main(String[] args) throws Exception {
        USSDApplicationBuilder builder = USSDApplicationBuilder.builder();
        USSDState rootMenu = new RootMenuUSSDState();
        USSDState accountsMenu = new AccountsMenu();
        USSDState statementMenu = new StatementMenuUSSDState();
        USSDState accountDetailState = new AccountDetailState();

        builder.withInitialState(rootMenu);
        builder.withState(accountsMenu);
        builder.withState(statementMenu);
        builder.withState(accountDetailState);
        builder.withMenuTransition(rootMenu, accountsMenu, RootMenuItems.ACCOUNTS);
        builder.withMenuTransition(accountsMenu, accountDetailState, RootMenuItems.ACCOUNT_DETAIL);
        builder.withMenuTransition(rootMenu, statementMenu, RootMenuItems.STATEMENT);

        USSDApplication ussdApplication = builder.build();
        String result;
        result = ussdApplication.start();
        System.out.println("start\n" + result);

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

