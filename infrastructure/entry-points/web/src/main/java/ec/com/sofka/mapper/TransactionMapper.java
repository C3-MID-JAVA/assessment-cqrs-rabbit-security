package ec.com.sofka.mapper;

import ec.com.sofka.dto.GetTransactionByAccountRequestDTO;
import ec.com.sofka.dto.TransactionRequestDTO;
import ec.com.sofka.dto.TransactionResponseDTO;
import ec.com.sofka.transaction.commands.CreateTransactionCommand;
import ec.com.sofka.transaction.queries.query.GetAllByAccountNumberQuery;
import ec.com.sofka.transaction.queries.responses.TransactionResponse;

public class TransactionMapper {
    public static TransactionResponseDTO fromEntity(TransactionResponse transactionResponse) {
        return new TransactionResponseDTO(
                transactionResponse.getOperationId(),
                transactionResponse.getFee(),
                transactionResponse.getNetAmount(),
                transactionResponse.getType(),
                transactionResponse.getTimestamp(),
                transactionResponse.getCustomerId()
        );
    }

    public static CreateTransactionCommand toEntity(TransactionRequestDTO transactionRequestDTO) {
        return new CreateTransactionCommand(transactionRequestDTO.getAmount(), transactionRequestDTO.getType(), transactionRequestDTO.getAccountNumber(), transactionRequestDTO.getCustomerId());
    }

    public static GetAllByAccountNumberQuery toGetAllByAccount(GetTransactionByAccountRequestDTO getTransactionByAccountRequestDTO){
        return new GetAllByAccountNumberQuery(getTransactionByAccountRequestDTO.getCustomerId(), getTransactionByAccountRequestDTO.getAccountNumber());
    }
}
