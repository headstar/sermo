package demo.web;

import com.headstartech.sermo.screen.*;
import com.headstartech.sermo.statemachine.actions.OnItemHandlers;
import com.headstartech.sermo.statemachine.actions.PagedMenuItemsUtil;
import com.headstartech.sermo.statemachine.actions.PagedMenuSetupProvider;
import com.headstartech.sermo.statemachine.actions.PagedScreenSetupProvider;
import org.springframework.statemachine.StateContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PagedAccountsSetupProvider implements PagedScreenSetupProvider<States, SubscriberEvent> {

    @Override
    public PagedScreenSetup getPagedScreenSetup(StateContext<States, SubscriberEvent> context) {
        context.getExtendedState().getVariables().put(Constants.ACCOUNT_DATA_KEY, new AccountData());

        List<MenuItem> items = getAccountDetailsDTOs().stream()
                .map(accountDetailDTO -> new MenuItem(accountDetailDTO.getAccountId(), Transitions.ACCOUNT_DETAIL,
                        OnItemHandlers.modifyExtendedStateVariable(Constants.ACCOUNT_DATA_KEY, AccountData.class,
                                (accountData) ->  accountData.setAccountDetailsDTO(accountDetailDTO)))
                )
                .collect(Collectors.toList());

        return new DefaultPagedScreenSetup(PagedMenuItemsUtil.getScreenBlockForMenuItems(items, 2, new TextElide()),
                getNextScreenItem(), getPreviousScreenItem(), getHeaderBlock(), null);
    }

    private ScreenBlock getHeaderBlock() {
        return new Text("Accounts");
    }

    private NextPageMenuItem getNextScreenItem() {
        return new NextPageMenuItem("0", "Next page");
    }

    private PreviousPageMenuItem getPreviousScreenItem() {
        return new PreviousPageMenuItem("#", "Previous page");
    }

    private List<AccountDetailsDTO> getAccountDetailsDTOs() {
        List<AccountDetailsDTO> accountDetails = new ArrayList<>();
        accountDetails.add(AccountDetailsDTO.of("A", 7));
        accountDetails.add(AccountDetailsDTO.of("B", 11));
        accountDetails.add(AccountDetailsDTO.of("C", 15));
        accountDetails.add(AccountDetailsDTO.of("D", 21));
        accountDetails.add(AccountDetailsDTO.of("E", 25));
        return accountDetails;
    }
}
