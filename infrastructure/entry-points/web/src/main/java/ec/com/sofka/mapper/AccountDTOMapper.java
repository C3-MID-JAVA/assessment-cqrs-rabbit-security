package ec.com.sofka.mapper;


import ec.com.sofka.commands.CreateAccountCommand;
import ec.com.sofka.data.AccountReqDTO;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.queries.responses.account.CreateAccountResponse;

public class AccountDTOMapper {
    public static AccountDTO toAccountDTO(AccountReqDTO accountResponseDTO) {

        if (accountResponseDTO == null) {
            return null;
        }

        return new AccountDTO( accountResponseDTO.getAccountBalance(),accountResponseDTO.getAccountOwner(),
                accountResponseDTO.getAccountNumber(), accountResponseDTO.getAccountType());
    }

    public static AccountReqDTO toAccountReqDTO(AccountDTO account) {
        if (account == null) return null;
        return new AccountReqDTO(account.getAccountNumber(),account.getBalance(), account.getAccountType(), account.getOwnerName(), null);
    }

    public static AccountReqDTO fromCtoAccountReqDTO(CreateAccountResponse account) {
        if (account == null) return null;
        return new AccountReqDTO(account.getAccountNumber(),account.getAccountBalance(), account.getAccountType(), account.getOwnerName(), account.getCustomerId());
    }

    public static CreateAccountCommand toCreateAccountCommand(AccountReqDTO accountReqDTO) {
        if (accountReqDTO == null) return null;
        return new CreateAccountCommand(
                accountReqDTO.getAccountBalance(),
                accountReqDTO.getAccountNumber(),
                accountReqDTO.getAccountOwner(),
                accountReqDTO.getAccountType()
        );

    }



}

