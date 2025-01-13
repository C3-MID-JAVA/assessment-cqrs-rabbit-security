package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.generics.interfaces.IUseCaseGetEmpty;
import ec.com.sofka.generics.utils.QueryResponse;
import reactor.core.publisher.Mono;


public class GetAllAccountsUseCase implements IUseCaseGetEmpty<AccountResponse> {

    private final IAccountRepository repository;
    private final IBusEvent busMessage;

    private final IEventStore eventRepository;


    public GetAllAccountsUseCase(IAccountRepository repository, IBusEvent busMessage, IEventStore eventRepository) {
        this.repository = repository;
        this.busMessage = busMessage;
        this.eventRepository = eventRepository;
    }


    @Override
    public Mono<QueryResponse<AccountResponse>> get() {
        return repository.findAll()
                .map(accountDTO -> new AccountResponse(
                        accountDTO.getAccountId(),
                        accountDTO.getAccountId(),
                        accountDTO.getAccountNumber(),
                        accountDTO.getName(),
                        accountDTO.getBalance(),
                        accountDTO.getStatus()
                ))
                .collectList() // Convierte el Flux en una lista
                .map(QueryResponse::ofMultiple); // Envuelve la lista en un QueryResponse
    }



}
