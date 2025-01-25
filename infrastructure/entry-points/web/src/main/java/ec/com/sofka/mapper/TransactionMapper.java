package ec.com.sofka.mapper;

import ec.com.sofka.dto.TransactionRequestDTO;
import ec.com.sofka.dto.TransactionResponseDTO;
import ec.com.sofka.transaction.commands.CreateTransactionCommand;
import ec.com.sofka.transaction.queries.responses.TransactionResponse;

public class TransactionMapper {
    public static TransactionResponseDTO fromEntity(TransactionResponse transactionResponse) {
        return new TransactionResponseDTO(
                transactionResponse.getId(),
                transactionResponse.getFee(),
                transactionResponse.getNetAmount(),
                transactionResponse.getType(),
                transactionResponse.getTimestamp(),
                transactionResponse.getAccountId()
        );
    }

    public static CreateTransactionCommand toEntity(TransactionRequestDTO transactionRequestDTO) {
        return new CreateTransactionCommand(transactionRequestDTO.getAmount(), transactionRequestDTO.getType(), transactionRequestDTO.getAccountNumber());
    }
}
