package demo.web;

import com.headstartech.sermo.statemachine.actions.OnItemHandlers;
import com.headstartech.sermo.statemachine.actions.PagedMenuScreenEntryAction;
import com.headstartech.sermo.screen.*;
import org.springframework.statemachine.StateContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Per Johansson
 */
public class AccountsEntryAction extends PagedMenuScreenEntryAction<States, SubscriberEvent> {

    @Override
    protected PagedScreenSetup getPagedScreenSetup(StateContext<States, SubscriberEvent> context) {

        List<MenuItem> items = getAccountDetailsDTOs().stream()
                .map(e -> new MenuItem(e.getAccountId(), Transitions.ACCOUNT_DETAIL,
                        OnItemHandlers.setVariable(Constants.ACCOUNT_DATA_KEY, e))
                )
                .collect(Collectors.toList());

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

    protected List<AccountDetailsDTO> getAccountDetailsDTOs() {
        List<AccountDetailsDTO> accountDetails = new ArrayList<>();
        accountDetails.add(AccountDetailsDTO.of("A", 7));
        accountDetails.add(AccountDetailsDTO.of("B", 11));
        accountDetails.add(AccountDetailsDTO.of("C", 15));
        accountDetails.add(AccountDetailsDTO.of("D", 21));
        accountDetails.add(AccountDetailsDTO.of("E", 25));
        return accountDetails;
    }
}
