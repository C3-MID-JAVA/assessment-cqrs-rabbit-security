package ec.com.sofka.mapper;

import ec.com.sofka.data.TransactionEntity;
import ec.com.sofka.gateway.dto.TransactionDTO;

public class TransactionMapperEntity {
    public static TransactionEntity toEntity(TransactionDTO transactionDTO) {
        return new TransactionEntity(
                transactionDTO.getId(),
                transactionDTO.getAmount(),
                transactionDTO.getFee(),
                transactionDTO.getNetAmount(),
                transactionDTO.getType(),
                transactionDTO.getTimestamp(),
                transactionDTO.getAccountId()
        );
    }

    public static TransactionDTO fromEntity(TransactionEntity transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getFee(),
                transaction.getNetAmount(),
                transaction.getType(),
                transaction.getTimestamp(),
                transaction.getAccountId()
        );
    }
}
