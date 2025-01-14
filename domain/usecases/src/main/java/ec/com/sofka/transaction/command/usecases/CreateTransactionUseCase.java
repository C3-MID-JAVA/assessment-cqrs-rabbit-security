package ec.com.sofka.transaction.commands.usecases;

import ec.com.sofka.BadRequestException;
import ec.com.sofka.NotFoundException;
import ec.com.sofka.TransactionStrategy;
import ec.com.sofka.TransactionStrategyFactory;
import ec.com.sofka.aggregate.customer.Customer;
import ec.com.sofka.aggregate.operation.Operation;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.transaction.commands.CreateTransactionCommand;
import ec.com.sofka.transaction.queries.responses.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateTransactionUseCase implements IUseCaseExecute<CreateTransactionCommand, TransactionResponse> {
    private final IEventStore repository;
    private final TransactionStrategyFactory strategyFactory;
    private final BusEvent busEvent;

    public CreateTransactionUseCase(IEventStore repository, TransactionStrategyFactory strategyFactory, BusEvent busEvent) {
        this.repository = repository;
        this.strategyFactory = strategyFactory;
        this.busEvent = busEvent;
    }

    @Override
    public Mono<TransactionResponse> execute(CreateTransactionCommand cmd) {
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

                            operation.getUncommittedEvents()
                                    .stream()
                                    .map(repository::save)
                                    .forEach(busEvent::sendEventTransactionCreated);

                            operation.markEventsAsCommitted();

                            customer.updateAccountBalance(
                                    accountDTO.getId(),
                                    balance,
                                    accountDTO.getAccountNumber(),
                                    accountDTO.getUserId()
                            );

                            customer.getUncommittedEvents()
                                    .stream()
                                    .map(repository::save)
                                    .forEach(busEvent::sendEventAccountUpdated);

                            customer.markEventsAsCommitted();

                            return Mono.just(new TransactionResponse(
                                    operation.getId().getValue(),
                                    operation.getTransaction().getAmount().getValue(),
                                    operation.getTransaction().getFee().getValue(),
                                    operation.getTransaction().getFinalAmount().getValue(),
                                    operation.getTransaction().getType().getValue(),
                                    operation.getTransaction().getTimestamp().getValue(),
                                    operation.getTransaction().getAccountId().getValue()
                            ));
                        }));
    }
}
