package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.ConflictException;
import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.commands.CreateTransactionCommand;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.mapper.Mapper;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.commands.UpdateAccountCommand;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.appservice.queries.usecases.GetAccountByAccountNumberUseCase;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.enums.OperationType;
import ec.com.sofka.strategy.process.CalculateFinalBalance;
import ec.com.sofka.strategy.process.GetTransactionStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class ProcessTransactionUseCase {

    private final GetAccountByAccountNumberUseCase getAccountByNumberUseCase;
    private final GetTransactionStrategy getTransactionStrategyUseCase;
    private final CalculateFinalBalance calculateFinalBalanceUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final ITransactionRepository transactionRepository;
    private final IEventStore repository;

    private final Predicate<BigDecimal> isSaldoInsuficiente = saldo -> saldo.compareTo(BigDecimal.ZERO) < 0;

    public ProcessTransactionUseCase(GetAccountByAccountNumberUseCase getAccountByNumberUseCase,
                                     GetTransactionStrategy getTransactionStrategyUseCase,
                                     CalculateFinalBalance calculateFinalBalanceUseCase,
                                     UpdateAccountUseCase updateAccountUseCase, ITransactionRepository transactionRepository, IEventStore repository) {
        this.getAccountByNumberUseCase = getAccountByNumberUseCase;
        this.getTransactionStrategyUseCase = getTransactionStrategyUseCase;
        this.calculateFinalBalanceUseCase = calculateFinalBalanceUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.transactionRepository = transactionRepository;
        this.repository = repository;
    }

    public Mono<TransactionResponse> apply(CreateTransactionCommand cmd, OperationType operationType) {
        GetByElementQuery accountNumberRequest = new GetByElementQuery(cmd.getCustomerId(), cmd.getAccountNumber());
        return getAccountByNumberUseCase.get(accountNumberRequest)
                .switchIfEmpty(Mono.error(new ConflictException("Account not found")))
                .flatMap(queryResponse -> {
                    return Mono.justOrEmpty(queryResponse.getSingleResult())
                            .switchIfEmpty(Mono.error(new ConflictException("Account not found in query response.")))
                            .flatMap(accountResponse -> {
                                Account account = Mapper.accountResponseToAccount(accountResponse);

                                Customer customer = new Customer();

                                return getTransactionStrategyUseCase.apply(account, cmd.getTransactionType(), operationType, cmd.getAmount())
                                        .flatMap(strategy -> {
                                            BigDecimal finalBalance = calculateFinalBalanceUseCase.apply(
                                                    account.getBalance().getValue(),
                                                    cmd.getAmount(),
                                                    strategy.getAmount(),
                                                    operationType
                                            );

                                            if (isSaldoInsuficiente.test(finalBalance)) {
                                                return Mono.error(new ConflictException("Insufficient balance for transaction."));
                                            }

                                            UpdateAccountCommand updateAccountRequest = new UpdateAccountCommand(
                                                    cmd.getAggregateId(),
                                                    finalBalance,
                                                    account.getAccountNumber().getValue(),
                                                    account.getOwner().getValue(),
                                                    account.getStatus().getValue()
                                            );

                                            return updateAccountUseCase.execute(updateAccountRequest)
                                                    .flatMap(updatedAccountResponse -> {
                                                        customer.createTransaction(
                                                                cmd.getAmount(),
                                                                strategy.getAmount(),
                                                                LocalDateTime.now(),
                                                                cmd.getTransactionType(),
                                                                cmd.getCustomerId()
                                                        );

                                                        TransactionDTO transactionDTO = new TransactionDTO(
                                                                cmd.getAggregateId(),
                                                                cmd.getAmount(),
                                                                strategy.getAmount(),
                                                                LocalDateTime.now(),
                                                                cmd.getTransactionType(),
                                                                cmd.getCustomerId()
                                                        );

                                                        return transactionRepository.save(transactionDTO)
                                                                .flatMap(savedTransaction -> {
                                                                    return Flux.fromIterable(customer.getUncommittedEvents())
                                                                            .flatMap(repository::save)
                                                                            .then(Mono.just(savedTransaction));
                                                                })
                                                                .map(savedTransaction -> {
                                                                    customer.markEventsAsCommitted();
                                                                    return new TransactionResponse(
                                                                            cmd.getAggregateId(),
                                                                            savedTransaction.getTransactionId(),
                                                                            savedTransaction.getAccountId(),
                                                                            savedTransaction.getTransactionCost(),
                                                                            savedTransaction.getAmount(),
                                                                            savedTransaction.getDate(),
                                                                            savedTransaction.getType()
                                                                    );
                                                                });
                                                    });
                                        });
                            });
                });
    }


}
