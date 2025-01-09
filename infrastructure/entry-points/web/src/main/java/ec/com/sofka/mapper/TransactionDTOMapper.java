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


    public static TransactionResponse toTransactionResponse(TransactionRequestDTO transactionRequestDTO, String transactionId, String accountId, BigDecimal transactionCost) {
        // Asignamos valores de TransactionRequestDTO a TransactionResponse
        LocalDateTime transactionDate = LocalDateTime.now(); // Asumimos la fecha actual para la transacción

        return new TransactionResponse(
                transactionId,      // Este valor se debe generar o recibir como parámetro
                accountId,          // Este valor se debe obtener de la cuenta asociada
                transactionCost,    // Este valor se debe calcular en base a la lógica de transacción
                transactionRequestDTO.getAmount(),
                transactionDate,
                transactionRequestDTO.getTransactionType()
        );
    }


    public static CreateTransactionCommand toCreateTransactionRequest(TransactionRequestDTO transactionRequestDTO) {
        // Generar la fecha de la transacción (por ejemplo, la fecha actual)
        LocalDateTime transactionDate = LocalDateTime.now();
        return new CreateTransactionCommand(
                transactionRequestDTO.getAggregateId(),
                null,           // Este valor se debe calcular o proporcionar
                transactionRequestDTO.getAmount(),  // El monto de la transacción
                null,           // La fecha de la transacción
                transactionRequestDTO.getTransactionType(), // El tipo de transacción
                transactionRequestDTO.getAccountNumber()                  // ID de la cuenta asociado (proporcionado como parámetro)
        );


    }


    public static TransactionResponseDTO toTransactionResponseDTO(CreateTransactionCommand request) {
        return new TransactionResponseDTO(
                null,
                request.getAccountId(),
                request.getTransactionCost(),
                request.getAmount(),
                request.getTransactionDate(),
                request.getTransactionType()
        );

    }

}
