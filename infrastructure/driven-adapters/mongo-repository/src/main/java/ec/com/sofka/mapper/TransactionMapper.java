package ec.com.sofka.mapper;

import ec.com.sofka.data.TransactionEntity;
import ec.com.sofka.gateway.dto.TransactionDTO;

public class TransactionMapper {
    public static TransactionEntity toEntity(TransactionDTO transactionDTO) {
        return new TransactionEntity(
                transactionDTO.getId(),
                transactionDTO.getAmount(),
                transactionDTO.getType(),
                transactionDTO.getCost(),
                transactionDTO.getIdAccount(),
                transactionDTO.getStatus()
        );
    }

    public static TransactionDTO toDTO(TransactionEntity transactionEntity) {
        return new TransactionDTO(
                transactionEntity.getId(),
                transactionEntity.getAmount(),
                transactionEntity.getType(),
                transactionEntity.getCost(),
                transactionEntity.getIdAccount(),
                transactionEntity.getStatus()
        );
    }
}
