package ec.com.sofka.mapper;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.gateway.dto.account.AccountDTO;

public class AccountMapper {
    public static AccountEntity toEntity(AccountDTO accountDTO) {
        if (accountDTO == null) return null;

        return new AccountEntity(
                accountDTO.getId(),
                accountDTO.getAccountNumber(),
                accountDTO.getBalance(),
                accountDTO.getOwnerName(),
                accountDTO.getAccountType()
                );
    }

    public static AccountDTO toDTO(AccountEntity accountEntity) {
        if (accountEntity == null) return null;
        return new AccountDTO(
                accountEntity.getId(),
                accountEntity.getBalance(),
                accountEntity.getOwnerName(),
                accountEntity.getAccountNumber(),
                accountEntity.getAccountType()
        );
    }
}
