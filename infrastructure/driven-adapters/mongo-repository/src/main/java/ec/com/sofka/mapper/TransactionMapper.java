package ec.com.sofka.mapper;

import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.data.TransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionMapper {

    public static TransactionDTO transactionEntityToTransaction(TransactionEntity transactionEntity) {
        return new TransactionDTO(
                transactionEntity.getId(),
                transactionEntity.getAmount(),
                transactionEntity.getTransactionCost(),
                transactionEntity.getDate(),
                transactionEntity.getType(),
                transactionEntity.getAccountId()
        );
    }

    public static TransactionEntity transactionToAccountEntity(TransactionDTO transaction) {
        return new TransactionEntity(
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getTransactionCost(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getAccountId()
        );
    }

}
