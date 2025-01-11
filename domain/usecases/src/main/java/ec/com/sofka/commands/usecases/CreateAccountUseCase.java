package ec.com.sofka.commands.usecases;
import ec.com.sofka.aggregate.Customer;
import ec.com.sofka.commands.CreateAccountCommand;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.queries.responses.CreateAccountResponse;

//Usage of the IUseCase interface
public class CreateAccountUseCase implements IUseCaseExecute<CreateAccountCommand, CreateAccountResponse> {
    private final IEventStore repository;
    private final BusEvent busEvent;

    public CreateAccountUseCase(IEventStore repository, BusEvent busEvent) {
        this.repository = repository;
        this.busEvent = busEvent;
    }

    //Of course, you have to create that class Response in usecases module on a package called responses or you can also group the command with their response class in a folder (Screaming architecture)
    //You maybe want to check Jacobo's repository to see how he did it
    @Override
    public CreateAccountResponse execute(CreateAccountCommand request) {
        //Create the aggregate, remember this usecase is to create the account the first time so just have to create it.
        Customer customer = new Customer();

        //Then we create the account
        customer.createAccount(request.getNumber(), request.getBalance(),  request.getCustomerName(),request.getStatus(),request.getIdUser());


        //Last step for events to be saved
        //Oh, you look someone who would like rabbits because if the save is done correctly,
        // I can send the message to the queue
        customer.getUncommittedEvents()
                .stream()
                        .map(repository::save)
                                .forEach(busEvent::sendEvent);


        //Then, call this stuff
        customer.markEventsAsCommitted();

        //Return the response
        return new CreateAccountResponse(
                customer.getId().getValue(),
                customer.getAccount().getId().getValue(),
                customer.getAccount().getNumber().getValue(),
                customer.getAccount().getName().getValue(),
                customer.getAccount().getBalance().getValue(),
                customer.getAccount().getStatus().getValue(),
                customer.getAccount().getUserId().getValue()
        );
    }
}
