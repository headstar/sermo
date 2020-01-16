package com.headstartech.sermo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SampleApp {

	@Test
    public void test() throws Exception {

		USSDApplicationBuilder builder = USSDApplicationBuilder.builder();
		USSDState rootMenu = new RootMenuUSSDState();
		USSDState balanceMenu = new BalanceMenuUSSDState();
		USSDState statementMenu = new StatementMenuUSSDState();
		USSDState accountDetailState = new AccountDetailState();

		builder.withInitialState(rootMenu);
		builder.withState(balanceMenu);
		builder.withState(statementMenu);
		builder.withState(accountDetailState);
		builder.withMenuTransition(rootMenu, balanceMenu, RootMenuItems.BALANCE);
		builder.withMenuTransition(rootMenu, statementMenu, RootMenuItems.STATEMENT);
		builder.withMenuTransition(balanceMenu, accountDetailState, RootMenuItems.ACCOUNT_DETAIL);

		USSDApplication ussdApplication = builder.build();
		String result;
		result = ussdApplication.start();
		System.out.println("start\n" + result);
		result = ussdApplication.applyEvent(new MOInput("1"));
		System.out.println("1\n" + result);
		result = ussdApplication.applyEvent(new MOInput("3"));
		System.out.println("3\n" + result);
	}
}
