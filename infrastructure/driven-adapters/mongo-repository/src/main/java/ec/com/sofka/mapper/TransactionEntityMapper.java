package ec.com.sofka.mapper;

import ec.com.sofka.data.TransactionEntity;
import ec.com.sofka.gateway.dto.TransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntityMapper {

    public static TransactionEntity mapToEntity(TransactionDTO transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionEntity(
                transaction.getId(),
                transaction.getAccountNumber(),
                transaction.getDetails(),
                transaction.getAmount(),
                transaction.getProcessingDate(),
                AccountEntityMapper.mapToEntity(transaction.getAccount()),
                TransactionTypeEntityMapper.mapToEntity(transaction.getTransactionType()));

    }

    public static TransactionDTO mapToDTO(TransactionEntity transactionEntity) {
        if (transactionEntity == null) {
            return null;
        }

        return new TransactionDTO(
                transactionEntity.getAccountNumber(),
                transactionEntity.getDetails(),
                transactionEntity.getAmount(),
                transactionEntity.getProcessingDate(),
                AccountEntityMapper.mapToDTO(transactionEntity.getAccount()),
                TransactionTypeEntityMapper.mapToDTO(transactionEntity.getTransactionType())
        );

    }

}