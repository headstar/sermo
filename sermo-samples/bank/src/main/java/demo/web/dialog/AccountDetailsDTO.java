package demo.web.dialog;

/**
 * @author Per Johansson
 */
public class AccountDetailsDTO {

    private final String accountId;
    private final Integer amount;

    public AccountDetailsDTO(String accountId, Integer amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public Integer getAmount() {
        return amount;
    }

    public static AccountDetailsDTO of(String accountId, Integer amount) {
        return new AccountDetailsDTO(accountId, amount);
    }
}
