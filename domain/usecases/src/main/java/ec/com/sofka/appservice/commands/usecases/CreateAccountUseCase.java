package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.ConflictException;
import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.appservice.commands.CreateAccountCommand;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.generics.interfaces.IUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateAccountUseCase implements IUseCase <CreateAccountCommand, AccountResponse>{

    private final IAccountRepository accountRepository;
    private final IEventStore eventRepository;
    private final IBusEvent busEvent;
    public CreateAccountUseCase(IAccountRepository accountRepository, IEventStore eventRepository, IBusEvent busEvent) {
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
        this.busEvent = busEvent;
    }

    @Override
    public Mono<AccountResponse> execute(CreateAccountCommand cmd) {
        return accountRepository.findByAccountNumber(cmd.getNumber())
                .flatMap(existingAccount -> Mono.<AccountResponse>error(new ConflictException("The account number is already registered.")))
                .switchIfEmpty(Mono.defer(() -> {
                    Customer customer = new Customer();
                    customer.createAccount(cmd.getNumber(), cmd.getBalance(), cmd.getCustomerName(), cmd.getStatus());
                    if (customer == null) {
                        return Mono.error(new RuntimeException("Customer not found"));
                    }

                    customer.getUncommittedEvents().forEach(event -> {
                        eventRepository.save(event).subscribe();
                        busEvent.sendEventAccountCreated(Mono.just(event));
                    });
                    customer.markEventsAsCommitted();
                    return Mono.just(
                            new AccountResponse(
                                    customer.getId().getValue(),
                                    customer.getAccount().getId().getValue(),
                                    customer.getAccount().getAccountNumber().getValue(),
                                    customer.getAccount().getOwner().getValue(),
                                    customer.getAccount().getBalance().getValue(),
                                    customer.getAccount().getStatus().getValue())
                    );
                }));
    }

}
