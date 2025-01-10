package ec.com.sofka.mapper;

import ec.com.sofka.account.commands.CreateAccountCommand;
import ec.com.sofka.dto.AccountRequestDTO;
import ec.com.sofka.dto.AccountResponseDTO;
import ec.com.sofka.account.responses.AccountResponse;

public class AccountMapper {
    public static AccountResponseDTO fromEntity(AccountResponse accountResponse) {
        return new AccountResponseDTO(
                accountResponse.getCustomerId(),
                accountResponse.getAccountNumber(),
                accountResponse.getBalance(),
                accountResponse.getUserId());
    }

    public static CreateAccountCommand toEntity(AccountRequestDTO accountRequestDTO) {
        return new CreateAccountCommand(accountRequestDTO.getAggregateId());
    }
}
