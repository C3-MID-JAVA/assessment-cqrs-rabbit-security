package ec.com.sofka.queries.responses.account;

import ec.com.sofka.account.Account;

public class GetAccountByNumResponse {
    private Account account;
    private CreateAccountResponse createAccountResponse;

    public GetAccountByNumResponse(Account account, CreateAccountResponse createAccountResponse) {
        this.account = account;
        this.createAccountResponse = createAccountResponse;
    }

    public Account getAccount() {
        return account;
    }

    public CreateAccountResponse getCreateAccountResponse() {
        return createAccountResponse;
    }
}
