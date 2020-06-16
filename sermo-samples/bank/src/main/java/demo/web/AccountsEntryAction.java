package demo.web;

import com.headstartech.sermo.statemachine.actions.PagedMenuScreenEntryAction;
import com.headstartech.sermo.screen.*;
import org.springframework.statemachine.StateContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Per Johansson
 */
public class AccountsEntryAction extends PagedMenuScreenEntryAction<States, SubscriberEvent> {

    @Override
    protected PagedScreenSetup getPagedScreenSetup(StateContext<States, SubscriberEvent> context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Account A", Transitions.ACCOUNT_DETAIL, AccountDetailsDTO.of("A", 7)));
        items.add(new MenuItem("Account B", Transitions.ACCOUNT_DETAIL, AccountDetailsDTO.of("B", 17)));
        items.add(new MenuItem("Account C", Transitions.ACCOUNT_DETAIL, AccountDetailsDTO.of("C", 15)));
        items.add(new MenuItem("Account D", Transitions.ACCOUNT_DETAIL, AccountDetailsDTO.of("D", 21)));
        items.add(new MenuItem("Account E", Transitions.ACCOUNT_DETAIL, AccountDetailsDTO.of("E", 25)));
        return new PagedScreenSetup(items, getNextScreenItem(), getPreviousScreenItem(), getHeaderBlock(), null, 2);
    }


    protected ScreenBlock getHeaderBlock() {
        return new Text("Accounts");
    }

    protected NextPageMenuItem getNextScreenItem() {
        return new NextPageMenuItem("0", "Next page");
    }

    protected PreviousPageMenuItem getPreviousScreenItem() {
        return new PreviousPageMenuItem("#", "Previous page");
    }
}
