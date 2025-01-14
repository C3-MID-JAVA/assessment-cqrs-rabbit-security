package ec.com.sofka.mapper;

import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.commands.AccountCommand;
import ec.com.sofka.queries.responses.AccountResponse;
import ec.com.sofka.utils.enums.StatusEnum;
import org.springframework.stereotype.Component;

@Component
public class AccountModelMapper {

    public static AccountCommand mapToRequest(AccountRequestDTO account) {
        if (account == null) {
            return null;
        }

        return new AccountCommand(
                account.getAccountNumber(),
                account.getBalance(),
                StatusEnum.valueOf(account.getStatus()),
                account.getCustomerAggregateId()
    );

    }

    public static AccountResponseDTO mapToDTO(AccountResponse account) {
        if (account == null) {
            return null;
        }

        return new AccountResponseDTO(
                account.getNumber(),
                account.getBalance(),
                account.getStatus().name(),
                CustomerModelMapper.mapToDTO(account.getUser())
        );
    }

}