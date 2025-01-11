package ec.com.sofka.commands.usecases;

import ec.com.sofka.account.values.AccountEnum;
import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.commands.UpdateAccountCommand;
import ec.com.sofka.queries.responses.UpdateAccountResponse;

import java.util.List;

public class DeleteAccountUseCase implements IUseCaseExecute<UpdateAccountCommand, UpdateAccountResponse> {
    private final AccountRepository accountRepository;
    private final IEventStore eventRepository;

    public DeleteAccountUseCase(AccountRepository accountRepository, IEventStore eventRepository) {
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public UpdateAccountResponse execute(UpdateAccountCommand request) {
        //Get events related to the aggregateId on the request
        List<DomainEvent> events = eventRepository.findAggregate(request.getAggregateId());

        //Rebuild the aggregate
        Customer customer = Customer.from(request.getAggregateId(),events);

        customer.updateAccount(
                customer.getAccount().getId().getValue(),
                customer.getAccount().getBalance().getValue(),
                customer.getAccount().getNumber().getValue(),
                customer.getAccount().getName().getValue(),
                AccountEnum.ACCOUNT_INACTIVE.name(),
                customer.getAccount().getUserId().getValue()
        );

        //"Delete" the account
        AccountDTO result = accountRepository.delete(
                new AccountDTO(customer.getAccount().getId().getValue(),
                        customer.getAccount().getNumber().getValue(),
                        customer.getAccount().getName().getValue(),
                        customer.getAccount().getBalance().getValue(),
                        customer.getAccount().getStatus().getValue()
                ));

        if (result != null) {
            //Last step for events to be saved
            customer.getUncommittedEvents().forEach(eventRepository::save);

            customer.markEventsAsCommitted();

            return new UpdateAccountResponse(
                    request.getAggregateId(),
                    result.getId(),
                    result.getAccountNumber(),
                    result.getName(),
                    result.getStatus());
        }

        return new UpdateAccountResponse();
    }
}
