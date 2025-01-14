package ec.com.sofka.appservice.mapper;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.NumberAcc;
import ec.com.sofka.account.values.objects.Owner;
import ec.com.sofka.account.values.objects.Status;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.appservice.gateway.dto.TransactionDTO;

import java.math.BigDecimal;

public class Mapper {

    public static Account accountResponseToAccount(AccountResponse accountResponse) {
        Balance balance = Balance.of(accountResponse.getBalance());
        AccountId accountId = AccountId.of(accountResponse.getAccountId());
        NumberAcc numberAcc = NumberAcc.of(accountResponse.getAccountNumber());
        Owner owner = Owner.of(accountResponse.getName());
        Status status = Status.of(accountResponse.getStatus());

        return new Account(accountId, balance, numberAcc, owner, status);
    }
}
