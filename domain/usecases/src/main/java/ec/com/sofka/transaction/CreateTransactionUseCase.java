package ec.com.sofka.transaction;

import ec.com.sofka.TransactionStrategy;
import ec.com.sofka.TransactionStrategyFactory;
import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.aggregate.operation.Operation;
import ec.com.sofka.exception.BadRequestException;
import ec.com.sofka.exception.NotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.transaction.request.CreateTransactionRequest;
import ec.com.sofka.transaction.responses.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateTransactionUseCase implements IUseCase<CreateTransactionRequest, TransactionResponse> {
    private final IEventStore repository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionStrategyFactory strategyFactory;
    private final BusEvent busEvent;

    public CreateTransactionUseCase(
            IEventStore repository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionStrategyFactory strategyFactory,
            BusEvent busEvent
    ) {
        this.busEvent = busEvent;
        this.repository = repository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public Mono<TransactionResponse> execute(CreateTransactionRequest cmd) {
        Flux<DomainEvent> eventsCustomer = repository.findAggregate(cmd.getCustomerId(), "customer");

        return Customer.from(cmd.getCustomerId(), eventsCustomer)
                .flatMap(customer -> Mono.justOrEmpty(
                                customer.getAccounts().stream()
                                        .filter(account -> account.getAccountNumber().getValue().equals(cmd.getAccountNumber()))
                                        .findFirst()
                        )
                        .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                        .flatMap(account -> {
                            AccountDTO accountDTO = new AccountDTO(
                                    account.getId().getValue(),
                                    account.getAccountNumber().getValue(),
                                    account.getBalance().getValue(),
                                    account.getUserId().getValue()
                            );

                            Operation operation = new Operation();

                            TransactionStrategy strategy = strategyFactory.getStrategy(cmd.getType());
                            BigDecimal fee = strategy.calculateFee();
                            BigDecimal netAmount = cmd.getAmount().add(fee);
                            BigDecimal balance = strategy.calculateBalance(accountDTO.getBalance(), cmd.getAmount());

                            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                                throw new BadRequestException("Insufficient balance for this transaction.");
                            }

                            operation.createTransaction(
                                    cmd.getAmount(),
                                    fee,
                                    netAmount,
                                    LocalDateTime.now(),
                                    cmd.getType(),
                                    accountDTO.getId()
                            );

                            return transactionRepository.save(new TransactionDTO(
                                            operation.getTransaction().getId().getValue(),
                                            operation.getTransaction().getAmount().getValue(),
                                            operation.getTransaction().getFee().getValue(),
                                            operation.getTransaction().getNetAmount().getValue(),
                                            operation.getTransaction().getType().getValue(),
                                            operation.getTransaction().getTimestamp().getValue(),
                                            operation.getTransaction().getAccountId().getValue()
                                    ))
                                    .flatMap(transactionDTO -> {
                                        accountDTO.setBalance(balance);
                                        return accountRepository.save(accountDTO)
                                                .flatMap(savedAccount -> {
                                                    customer.updateAccountBalance(
                                                            savedAccount.getId(),
                                                            balance,
                                                            savedAccount.getAccountNumber(),
                                                            savedAccount.getUserId()
                                                    );

                                                    return Flux.concat(
                                                                    Flux.fromIterable(operation.getUncommittedEvents())
                                                                            .flatMap(event -> repository.save(event)
                                                                                    .doOnSuccess(savedEvent -> busEvent.sendEventTransactionCreated(Mono.just(savedEvent))) // Publicar evento
                                                                            ),
                                                                    Flux.fromIterable(customer.getUncommittedEvents())
                                                                            .flatMap(repository::save)
                                                            )
                                                            .doOnTerminate(() -> {
                                                                operation.markEventsAsCommitted();
                                                                customer.markEventsAsCommitted();
                                                            })
                                                            .then()
                                                            .thenReturn(new TransactionResponse(
                                                                    operation.getId().getValue(),
                                                                    operation.getTransaction().getAmount().getValue(),
                                                                    operation.getTransaction().getFee().getValue(),
                                                                    operation.getTransaction().getNetAmount().getValue(),
                                                                    operation.getTransaction().getType().getValue(),
                                                                    operation.getTransaction().getTimestamp().getValue(),
                                                                    operation.getTransaction().getAccountId().getValue(),
                                                                    customer.getId().getValue()
                                                            ));
                                                });
                                    });
                        }));
    }
}
