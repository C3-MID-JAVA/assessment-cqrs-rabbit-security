package ec.com.sofka.mapper;

import ec.com.sofka.account.commands.CreateAccountCommand;
import ec.com.sofka.account.queries.query.GetAccountByNumberQuery;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.dto.AccountRequestDTO;
import ec.com.sofka.dto.AccountResponseDTO;
import ec.com.sofka.dto.GetAccountByNumberRequestDTO;

public class AccountMapper {
    public static AccountResponseDTO fromEntity(AccountResponse accountResponse) {
        return new AccountResponseDTO(
                accountResponse.getId(),
                accountResponse.getAccountNumber(),
                accountResponse.getBalance(),
                accountResponse.getUserId());
    }

    public static CreateAccountCommand toEntity(AccountRequestDTO accountRequestDTO) {
        return new CreateAccountCommand(accountRequestDTO.getAggregateId());
    }

    public static GetAccountByNumberQuery toAccountByNumberQuery(GetAccountByNumberRequestDTO getAccountByNumberRequestDTO) {
        return new GetAccountByNumberQuery(getAccountByNumberRequestDTO.getAccountNumber());
    }
}
