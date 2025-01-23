package ec.com.sofka.mapper;

import ec.com.sofka.appservice.commands.CreateTransactionCommand;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.data.TransactionResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class TransactionDTOMapper {

    public static TransactionResponseDTO toTransactionResponseDTO(TransactionResponse transactionResponse) {
        return new TransactionResponseDTO(
                transactionResponse.getTransactionId(),
                transactionResponse.getAccountId(),
                transactionResponse.getTransactionCost(),
                transactionResponse.getAmount(),
                transactionResponse.getTransactionDate(),
                transactionResponse.getTransactionType()
        );
    }

    public static TransactionResponseDTO toListTransactionResponseDTO(TransactionResponse transactionResponse) {
        return new TransactionResponseDTO(
                transactionResponse.getTransactionId(),
                transactionResponse.getAccountId(),
                transactionResponse.getTransactionCost(),
                transactionResponse.getAmount(),
                transactionResponse.getTransactionDate(),
                transactionResponse.getTransactionType()
        );
    }
    public static CreateTransactionCommand toCreateTransactionRequest(TransactionRequestDTO transactionRequestDTO) {
        LocalDateTime transactionDate = LocalDateTime.now();
        return new CreateTransactionCommand(
                null,
                transactionRequestDTO.getAmount(),
                null,
                transactionRequestDTO.getTransactionType(),
                transactionRequestDTO.getAccountNumber()
        );


    }


}
