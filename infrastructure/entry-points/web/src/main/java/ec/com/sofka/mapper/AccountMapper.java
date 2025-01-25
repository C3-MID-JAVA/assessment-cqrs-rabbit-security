package ec.com.sofka.mapper;

import ec.com.sofka.account.commands.CreateAccountCommand;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.account.queries.responses.AccountUserResponse;
import ec.com.sofka.dto.AccountRequestDTO;
import ec.com.sofka.dto.AccountResponseDTO;
import ec.com.sofka.dto.AccountUserResponseDTO;

public class AccountMapper {
    public static AccountResponseDTO fromEntity(AccountResponse accountResponse) {
        return new AccountResponseDTO(
                accountResponse.getId(),
                accountResponse.getAccountNumber(),
                accountResponse.getBalance(),
                accountResponse.getUserId());
    }

    public static CreateAccountCommand toEntity(AccountRequestDTO accountRequestDTO) {
        return new CreateAccountCommand(accountRequestDTO.getUserId());
    }

    public static AccountUserResponseDTO fromEntityWithUser(AccountUserResponse accountUserResponse){
        return new AccountUserResponseDTO(
                accountUserResponse.getId(),
                accountUserResponse.getAccountNumber(),
                accountUserResponse.getBalance(),
                accountUserResponse.getUserId(),
                accountUserResponse.getUserResponse()
        );
    }
}
