package ec.com.sofka.mapper;

import ec.com.sofka.aggregates.account.events.AccountCreated;
import ec.com.sofka.aggregates.account.events.AccountUpdated;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.account.entities.account.values.objects.AccountNumber;
import ec.com.sofka.queries.responses.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public static Account mapToModelFromDTO(AccountDTO account) {
        if (account == null) {
            return null;
        }

        return new Account(
                AccountId.of(account.getId()),
                AccountNumber.of(account.getAccountNumber()),
                Amount.of(account.getBalance()),
                account.getStatus(),
                CustomerMapper.mapToModelFromDTO(account.getUser()));
    }

    public static AccountResponse mapToResponseFromModel(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountResponse(
                account.getAccountNumber().getValue(),
                account.getBalance().getValue(),
                account.getStatus(),
                CustomerMapper.mapToResponseFromModel(account.getUser()));
    }

    public static AccountResponse mapToResponseFromDTO(AccountDTO account) {
        if (account == null) {
            return null;
        }

        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                CustomerMapper.mapToResponseFromDTO(account.getUser()));
    }

    public static AccountDTO mapToDTOFromModel(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountDTO(
                account.getId().getValue(),
                account.getAccountNumber().getValue(),
                account.getBalance().getValue(),
                account.getStatus(),
                account.getUser() != null ? CustomerMapper.mapToDTOFromModel(account.getUser()) : null);
    }

    public static AccountDTO mapToDTOFromCreatedEvent(AccountCreated account) {
        if (account == null) {
            return null;
        }

        return new AccountDTO(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                CustomerMapper.mapToDTOFromModel(account.getUser()));
    }

    public static AccountDTO mapToDTOFromUpdatesEvent(AccountUpdated account) {
        if (account == null) {
            return null;
        }

        return new AccountDTO(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                CustomerMapper.mapToDTOFromModel(account.getUser()));
    }

}