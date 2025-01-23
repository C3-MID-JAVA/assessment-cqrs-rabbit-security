package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.ConflictException;
import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.EventsEnum;
import ec.com.sofka.appservice.commands.CreateTransactionCommand;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.mapper.Mapper;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.commands.UpdateAccountCommand;
import ec.com.sofka.appservice.queries.query.GetByQuery;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.appservice.queries.usecases.GetAccountByAccountNumberUseCase;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.enums.OperationType;
import ec.com.sofka.generics.domain.DomainEvent;
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
    private final IEventStore eventRepository;
    private final IBusEvent busEvent;

    private final Predicate<BigDecimal> isSaldoInsuficiente = saldo -> saldo.compareTo(BigDecimal.ZERO) < 0;

    public ProcessTransactionUseCase(GetAccountByAccountNumberUseCase getAccountByNumberUseCase,
                                     GetTransactionStrategy getTransactionStrategyUseCase,
                                     CalculateFinalBalance calculateFinalBalanceUseCase,
                                     UpdateAccountUseCase updateAccountUseCase,
                                     IEventStore eventRepository,
                                     IBusEvent busEvent) {
        this.getAccountByNumberUseCase = getAccountByNumberUseCase;
        this.getTransactionStrategyUseCase = getTransactionStrategyUseCase;
        this.calculateFinalBalanceUseCase = calculateFinalBalanceUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.eventRepository = eventRepository;
        this.busEvent = busEvent;
    }

    public Mono<TransactionResponse> apply(CreateTransactionCommand cmd, OperationType operationType) {
        GetByQuery accountNumberRequest = new GetByQuery(cmd.getAccountNumber());

        Mono<AccountCreated> accountCreatedEvent = eventRepository.findAllAggregateByEvent(EventsEnum.ACCOUNT_CREATED.name())
                .switchIfEmpty(Mono.empty())
                .map(event -> (AccountCreated) event)
                .filter(event -> event.getAccountNumber().equals(cmd.getAccountNumber()))
                .single();

        return accountCreatedEvent.flatMap(accountCreated -> {
            Flux<DomainEvent> eventsCustomer = eventRepository.findAggregate(accountCreated.getAggregateRootId());

            return Customer.from(accountCreated.getAggregateRootId(), eventsCustomer)
                    .flatMap(customer -> {
                        return getTransactionStrategyUseCase.apply(customer.getAccount(), cmd.getTransactionType(), operationType, cmd.getAmount())
                                .flatMap(strategy -> {
                                    BigDecimal finalBalance = calculateFinalBalanceUseCase.apply(
                                            customer.getAccount().getBalance().getValue(),
                                            cmd.getAmount(),
                                            strategy.getAmount(),
                                            operationType
                                    );

                                    if (isSaldoInsuficiente.test(finalBalance)) {
                                        return Mono.error(new ConflictException("Insufficient balance for transaction."));
                                    }

                                    UpdateAccountCommand updateAccountRequest = new UpdateAccountCommand(
                                            finalBalance,
                                            customer.getAccount().getAccountNumber().getValue(),
                                            customer.getAccount().getOwner().getValue(),
                                            customer.getAccount().getStatus().getValue()
                                    );

                                    return updateAccountUseCase.execute(updateAccountRequest)
                                            .flatMap(updatedAccountResponse -> {
                                                customer.createTransaction(
                                                        cmd.getAmount(),
                                                        strategy.getAmount(),
                                                        LocalDateTime.now(),
                                                        cmd.getTransactionType(),
                                                        updatedAccountResponse.getAccountId()
                                                );

                                                return Flux.fromIterable(customer.getUncommittedEvents())
                                                        .flatMap(event -> {
                                                            // Guardar el evento
                                                            return eventRepository.save(event)
                                                                    // Enviar el evento por BusEvent
                                                                    .doOnNext(savedEvent ->
                                                                            busEvent.sendEventTransactionCreated(Mono.just(savedEvent)));
                                                        })
                                                        .then(Mono.just(customer.getUncommittedEvents()));
                                            })
                                            .map(uncommittedEvents -> {
                                                customer.markEventsAsCommitted();
                                                return new TransactionResponse(
                                                        customer.getId().getValue(),
                                                        cmd.getAccountNumber(),
                                                        strategy.getAmount(),
                                                        cmd.getAmount(),
                                                        LocalDateTime.now(),
                                                        cmd.getTransactionType()
                                                );
                                            });
                                });
                    });
        });
    }
            /*
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
                                                                updatedAccountResponse.getAccountId()
                                                        );

                                                        return Flux.fromIterable(customer.getUncommittedEvents())
                                                                .flatMap(event -> {
                                                                    // Guardar el evento
                                                                    return eventRepository.save(event)
                                                                            // Enviar el evento por BusEvent
                                                                            .doOnNext(savedEvent ->
                                                                                    busEvent.sendEventTransactionCreated(Mono.just(savedEvent)));
                                                                })
                                                                .then(Mono.just(customer.getUncommittedEvents()));
                                                    })
                                                    .map(uncommittedEvents -> {
                                                        customer.markEventsAsCommitted();
                                                        return new TransactionResponse(
                                                                customer.getId().getValue(),
                                                                cmd.getCustomerId(),
                                                                cmd.getAccountNumber(),
                                                                strategy.getAmount(),
                                                                cmd.getAmount(),
                                                                LocalDateTime.now(),
                                                                cmd.getTransactionType()
                                                        );
                                                    });
                                        });
                            });
                });*/

}
