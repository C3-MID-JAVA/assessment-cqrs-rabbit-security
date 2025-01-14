package ec.com.sofka.mapper;

import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.TransactionAccount;
import ec.com.sofka.aggregates.account.events.TransactionCreated;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.queries.responses.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public static TransactionDTO mapToDTOFromModel(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionDTO(
                transaction.getId().getValue(),
                transaction.getTransactionAccount().getValue(),
                transaction.getDetails(),
                transaction.getAmount().getValue(),
                transaction.getProcessingDate().getValue(),
                AccountMapper.mapToDTOFromModel(transaction.getAccount()),
                TransactionTypeMapper.mapToDTOFromModel(transaction.getTransactionType()));
    }

    public static Transaction mapToModelFromDTO(TransactionDTO transaction) {
        if (transaction == null) {
            return null;
        }

        return new Transaction(
                TransactionId.of(transaction.getId()),
                TransactionAccount.of(transaction.getAccountNumber()),
                transaction.getDetails(),
                Amount.of(transaction.getAmount()),
                ProcessingDate.of(transaction.getProcessingDate()),
                AccountMapper.mapToModelFromDTO(transaction.getAccount()),
                TransactionTypeMapper.mapToModelFromDTO(transaction.getTransactionType()));
    }

    public static TransactionResponse mapToResponseFromModel(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponse(
                transaction.getTransactionAccount().getValue(),
                transaction.getDetails(),
                transaction.getAmount().getValue(),
                transaction.getProcessingDate().getValue(),
                AccountMapper.mapToResponseFromModel(transaction.getAccount()),
                TransactionTypeMapper.mapToResponseFromModel(transaction.getTransactionType()));
    }


    public static TransactionDTO mapToDTOFromEvent(TransactionCreated transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionDTO(
                transaction.getTransactionId(),
                transaction.getAccountNumber(),
                transaction.getDetails(),
                transaction.getAmount(),
                transaction.getProcessingDate(),
                AccountMapper.mapToDTOFromModel(transaction.getAccount()),
                TransactionTypeMapper.mapToDTOFromModel(transaction.getTransactionType()));
    }

}