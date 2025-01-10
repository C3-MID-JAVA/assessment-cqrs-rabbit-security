package ec.com.sofka.mapper;

import ec.com.sofka.appservice.commands.CreateAccountCommand;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.commands.UpdateAccountCommand;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.queries.responses.UpdateAccountResponse;
import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;

public class AccountDTOMapper {
    // Convierte AccountRequestDTO a CreateAccountRequest
    public static CreateAccountCommand accountRequestDTOtoCreateAccountCommand(AccountRequestDTO requestDTO) {
        return new CreateAccountCommand(
                requestDTO.getAccountNumber(),
                requestDTO.getOwner(),
                requestDTO.getInitialBalance()
        );
    }

    // Convierte la respuesta del caso de uso a AccountResponseDTO
    public static AccountResponseDTO accountResponsetoAccountResponseDTO(AccountResponse response) {
        return new AccountResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        );
    }

    // Convierte AccountRequestDTO a UpdateAccountCommand
    public static UpdateAccountCommand accountRequestDTOtoUpdateAccountCommand(AccountRequestDTO requestDTO) {
        return new UpdateAccountCommand(
                requestDTO.getCustomerId(),
                requestDTO.getInitialBalance(),
                requestDTO.getAccountNumber(),
                requestDTO.getOwner(),
                requestDTO.getStatus()
        );
    }

    public static AccountResponse updateAccountResponseToAccountResponse(UpdateAccountResponse response) {
        return new AccountResponse(
                response.getCustomerId(),
                response.getAccountId(),
                response.getAccountNumber(),
                response.getName(),
                response.getBalance(),
                response.getStatus()
        );
    }

}
