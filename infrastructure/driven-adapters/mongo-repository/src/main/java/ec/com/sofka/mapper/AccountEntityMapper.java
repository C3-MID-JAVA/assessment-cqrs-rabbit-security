package ec.com.sofka.mapper;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.gateway.dto.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountEntityMapper {

    public static AccountEntity mapToEntity(AccountDTO account) {
        if (account == null) {
            return null;
        }

        return new AccountEntity(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getStatus(),
                CustomerEntityMapper.mapToEntity(account.getUser())
        );

    }

    public static AccountDTO mapToDTO(AccountEntity accountEntity) {
        if (accountEntity == null) {
            return null;
        }

        return new AccountDTO(
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getStatus(),
                CustomerEntityMapper.mapToDTO(accountEntity.getCustomer())
        );
    }

}