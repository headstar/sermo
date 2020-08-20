package demo.web;

import com.headstartech.sermo.screen.*;
import com.headstartech.sermo.statemachine.actions.OnItemHandlers;
import com.headstartech.sermo.statemachine.actions.PagedMenuSetupProvider;
import org.springframework.statemachine.StateContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PagedAccountsSetupProvider implements PagedMenuSetupProvider<States, SubscriberEvent> {

    @Override
    public PagedMenuSetup getPagedScreenSetup(StateContext<States, SubscriberEvent> context) {
        context.getExtendedState().getVariables().put(Constants.ACCOUNT_DATA_KEY, new AccountData());

        List<MenuItem> items = getAccountDetailsDTOs().stream()
                .map(accountDetailDTO -> new MenuItem(accountDetailDTO.getAccountId(), Transitions.ACCOUNT_DETAIL,
                        OnItemHandlers.modifyExtendedStateVariable(Constants.ACCOUNT_DATA_KEY, AccountData.class,
                                (accountData) ->  accountData.setAccountDetailsDTO(accountDetailDTO)))
                )
                .collect(Collectors.toList());

        return new PagedMenuSetup(items, getNextScreenItem(), getPreviousScreenItem(), getHeaderBlock(), null, 2);
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
