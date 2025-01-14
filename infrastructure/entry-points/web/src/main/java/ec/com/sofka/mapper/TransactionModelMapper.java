package ec.com.sofka.mapper;

import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import ec.com.sofka.commands.TransactionCommand;
import ec.com.sofka.queries.responses.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionModelMapper {

    public static TransactionCommand mapToRequest(TransactionRequestDTO transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionCommand(
                transaction.getAmount(),
                transaction.getProcessingDate(),
                transaction.getAccountNumber(),
                transaction.getDetails(),
                transaction.getTransactionTypeId(),
                transaction.getAccountAggregateId()
        );

    }

    public static TransactionResponseDTO mapToDTO(TransactionResponse transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponseDTO(
                transaction.getAccountNumber(),
                transaction.getDetails(),
                transaction.getAmount(),
                transaction.getProcessingDate(),
                AccountModelMapper.mapToDTO(transaction.getAccount()),
                TransactionTypeModelMapper.mapToDTO(transaction.getTransactionType())
        );
    }

}