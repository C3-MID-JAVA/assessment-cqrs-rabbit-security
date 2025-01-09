package ec.com.sofka.mapper;

import ec.com.sofka.appservice.commands.CreateAccountCommand;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.commands.UpdateAccountCommand;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;

public class AccountDTOMapper {
    // Convierte AccountRequestDTO a CreateAccountRequest
    public static CreateAccountCommand toCreateAccountRequest(AccountRequestDTO requestDTO) {
        return new CreateAccountCommand(
                requestDTO.getAccountNumber(),
                requestDTO.getOwner(),
                requestDTO.getInitialBalance()
        );
    }

    // Convierte la respuesta del caso de uso a AccountResponseDTO
    public static AccountResponseDTO toAccountResponseDTO(AccountResponse response) {
        return new AccountResponseDTO(
                response.getCustomerId(),
                response.getAccountId(),
                response.getName(),
                response.getAccountNumber(),
                response.getBalance(),
                response.getStatus()
        );
    }

    public static GetByElementQuery toGetAccountRequest(AccountRequestDTO request) {
        return new GetByElementQuery(
                request.getCustomerId(),
                request.getAccountNumber()
        );
    }

    public static UpdateAccountCommand toUpdateAccountRequest(AccountRequestDTO request) {
        return new UpdateAccountCommand(
                request.getCustomerId(),
                request.getInitialBalance(),
                request.getAccountNumber(),
                request.getOwner(),
                request.getStatus()
        );
    }
}
