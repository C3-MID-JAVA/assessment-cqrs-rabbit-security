package ec.com.sofka.mapper;

import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.data.AccountEntity;

import java.math.BigDecimal;

public class AccountMapper {
    public static AccountEntity DtoToEntity(AccountDTO accountDTO) {
        return new AccountEntity(
                accountDTO.getAccountId(),
                accountDTO.getName(),
                accountDTO.getAccountNumber(),
                accountDTO.getBalance(),
                accountDTO.getStatus()
        );
    }

    public static AccountDTO entityToDTO(AccountEntity accountEntity) {
        return new AccountDTO(
                accountEntity.getId(),
                accountEntity.getName(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getStatus()

        );
    }
}
