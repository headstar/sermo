package demo.web;

public class AccountData {

    private AccountDetailsDTO accountDetailsDTO;

    public AccountDetailsDTO getAccountDetailsDTO() {
        return accountDetailsDTO;
    }

    public void setAccountDetailsDTO(AccountDetailsDTO accountDetailsDTO) {
        this.accountDetailsDTO = accountDetailsDTO;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountData{");
        sb.append("accountDetailsDTO=").append(accountDetailsDTO);
        sb.append('}');
        return sb.toString();
    }
}
