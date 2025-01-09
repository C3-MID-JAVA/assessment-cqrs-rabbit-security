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
    private final IEventStore repository;
    private final IBusEvent busMessage;
    public CreateAccountUseCase(IAccountRepository accountRepository, IEventStore repository, IBusEvent busMessage) {
        this.accountRepository = accountRepository;
        this.repository = repository;
        this.busMessage = busMessage;
    }

    @Override
    public Mono<AccountResponse> execute(CreateAccountCommand cmd) {
        // Verificar si ya existe una cuenta con el mismo número
        return accountRepository.findByAccountNumber(cmd.getNumber())
                .flatMap(existingAccount -> Mono.<AccountResponse>error(new ConflictException("The account number is already registered.")))
                .switchIfEmpty(Mono.defer(() -> {
                    // Crear un cliente y una cuenta
                    Customer customer = new Customer();
                    customer.createAccount(cmd.getNumber(), cmd.getBalance(), cmd.getCustomerName(), cmd.getStatus());

                    // Guardar la cuenta en el repositorio
                    return accountRepository.save(
                                    new AccountDTO(
                                            customer.getAccount().getId().getValue(),
                                            customer.getAccount().getOwner().getValue(),
                                            customer.getAccount().getAccountNumber().getValue(),
                                            customer.getAccount().getBalance().getValue(),
                                            customer.getAccount().getStatus().getValue()
                                    )
                            ).onErrorResume(e -> {
                                // Maneja el error, por ejemplo, registrando el error
                                return Mono.error(new RuntimeException("Error al guardar la cuenta"));
                            })
                            .flatMap(savedAccount -> Flux.fromIterable(customer.getUncommittedEvents())
                                    .flatMap(repository::save)
                                    .then(
                                            Mono.just(new AccountResponse(
                                                            customer.getId().getValue(),
                                                            customer.getAccount().getId().getValue(),
                                                            customer.getAccount().getAccountNumber().getValue(),
                                                            customer.getAccount().getOwner().getValue(),
                                                            customer.getAccount().getBalance().getValue(),
                                                            customer.getAccount().getStatus().getValue())
                                    ))
                            )
                            .doOnSuccess(saved -> {
                                customer.markEventsAsCommitted();
                            });

                }));
    }

}
