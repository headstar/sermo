package demo.bank;

import com.headstartech.sermo.*;
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
        items.add(new MenuItem("Account A", Transitions.ACCOUNT_DETAIL, "a"));
        items.add(new MenuItem("Account B", Transitions.ACCOUNT_DETAIL, "b"));
        items.add(new MenuItem("Account C", Transitions.ACCOUNT_DETAIL, "c"));
        items.add(new MenuItem("Account D", Transitions.ACCOUNT_DETAIL, "d"));
        items.add(new MenuItem("Account E", Transitions.ACCOUNT_DETAIL, "e"));
        return new PagedScreenSetup(items, getNextScreenItem(), getPreviousScreenItem(), getHeaderBlock(), null, 2);
    }


    protected ScreenBlock getHeaderBlock() {
        return new Text("Accounts for 1234");
    }

    protected NextPageMenuItem getNextScreenItem() {
        return new NextPageMenuItem("0", "Next page");
    }

    protected PreviousPageMenuItem getPreviousScreenItem() {
        return new PreviousPageMenuItem("#", "Previous page");
    }
}
