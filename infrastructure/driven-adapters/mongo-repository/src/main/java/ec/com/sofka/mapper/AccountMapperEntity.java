package ec.com.sofka.mapper;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.UserEntity;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.AccountUserDTO;
import ec.com.sofka.gateway.dto.UserDTO;

public class AccountMapperEntity {
    public static AccountEntity toEntity(AccountDTO accountDTO) {
        return new AccountEntity(
                accountDTO.getId(),
                accountDTO.getAccountNumber(),
                accountDTO.getBalance(),
                accountDTO.getUserId()
        );
    }

    public static AccountDTO toDTO(AccountEntity accountEntity) {
        return new AccountDTO(
                accountEntity.getId(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getUserId()
        );
    }

    public static AccountUserDTO toDTOWithUser(AccountEntity accountEntity, UserEntity userEntity) {
        UserDTO userDTO = UserMapperEntity.fromEntity(userEntity);

        return new AccountUserDTO(
                accountEntity.getId(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getUserId(),
                userDTO
        );
    }
}
