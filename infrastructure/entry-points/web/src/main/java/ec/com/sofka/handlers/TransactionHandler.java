package ec.com.sofka.handlers;
import ec.com.sofka.commands.usecases.CreateTransactionUseCase;
import ec.com.sofka.data.transaction.TransactionRequestDTO;
import ec.com.sofka.data.transaction.TransactionResponseDTO;
import ec.com.sofka.enums.TypeTransaction;
import ec.com.sofka.exceptions.CuentaNoEncontradaException;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.GetAccountResponse;
import ec.com.sofka.queries.usecases.GetAccountByNumberUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class TransactionHandler {
    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetAccountByNumberUseCase getAccountByNumberUseCase;

    public TransactionHandler(CreateTransactionUseCase createTransactionUseCase, GetAccountByNumberUseCase getAccountByNumberUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getAccountByNumberUseCase = getAccountByNumberUseCase;
    }

    public Mono<TransactionResponseDTO> saveTransaction(TransactionRequestDTO transactionRequestDTO, String customerId){
        BigDecimal costo = obtenerCostoTipoTransaccion(transactionRequestDTO.getType());
        boolean esRetiro = esRetiro(transactionRequestDTO.getType());
        GetAccountQuery accountQuery= new GetAccountQuery(customerId, transactionRequestDTO.getIdAccount());

        return validateAccount(accountQuery)
                .flatMap(account -> createTransactionUseCase.validarTransaction2(account, transactionRequestDTO.getAmount(), transactionRequestDTO.getType(), costo, esRetiro, customerId))
                .map(transaction -> new TransactionResponseDTO(
                        transaction.getOperationId(),
                        transaction.getTransactionId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getCost(),
                        transaction.getIdAccount(),
                        transaction.getStatus()
                ));
    }

    private Mono<AccountDTO> validateAccount(GetAccountQuery requestAcc) {
        return getAccountByNumberUseCase.get(requestAcc)
                .switchIfEmpty(Mono.error(new CuentaNoEncontradaException("Cuenta no encontrada")))
                .map(queryResponse -> {
                    GetAccountResponse accountResponse = queryResponse.getSingleResult().get();
                    return new AccountDTO(
                            accountResponse.getAccountId(),
                            accountResponse.getName(),
                            accountResponse.getAccountNumber(),
                            accountResponse.getBalance(),
                            accountResponse.getStatus()
                    );
                });
    }

    private BigDecimal obtenerCostoTipoTransaccion(String tipo) {
        if (!TypeTransaction.validadorTipo.validar(tipo)) {
            throw new IllegalArgumentException("Tipo de transacción no válido: " + tipo);
        }
        return TypeTransaction.fromString(tipo).getCosto();
    }

    private boolean esRetiro(String tipo) {
        return tipo.startsWith("RETIRO") || tipo.equals("COMPRA_WEB") || tipo.equals("COMPRA_ESTABLECIMIENTO");
    }
}
