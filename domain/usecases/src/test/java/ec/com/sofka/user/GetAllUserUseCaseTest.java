package ec.com.sofka.user;

import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GetAllUserUseCaseTest {

    @Mock
    private IEventStore eventStore;

    private GetAllUserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAllUserUseCase(eventStore);
    }

    @Test
    void shouldReturnAllUsersSuccessfully() {
        DomainEvent user1Created = new UserCreated("user1", "John Doe", "DOC001");
        user1Created.setAggregateRootId("customer1");
        user1Created.setVersion(1L);

        DomainEvent user2Created = new UserCreated("user2", "Jane Doe", "DOC002");
        user2Created.setAggregateRootId("customer2");
        user2Created.setVersion(1L);

        DomainEvent user1Updated = new UserCreated("user1", "John Smith", "DOC001");
        user1Updated.setAggregateRootId("customer1");
        user1Updated.setVersion(2L);

        when(eventStore.findAllAggregate("customer"))
                .thenReturn(Flux.just(user1Created, user2Created, user1Updated));

        StepVerifier.create(useCase.execute())
                .expectNextMatches(response ->
                        response.getCustomerId().equals("customer2") &&
                                response.getName().equals("Jane Doe") &&
                                response.getDocumentId().equals("DOC002")
                )
                .expectNextMatches(response ->
                        response.getCustomerId().equals("customer1") &&
                                response.getName().equals("John Smith") &&
                                response.getDocumentId().equals("DOC001")
                )
                .verifyComplete();

        verify(eventStore, times(1)).findAllAggregate("customer");
    }

    @Test
    void shouldReturnEmptyFluxWhenNoUsersExist() {
        when(eventStore.findAllAggregate("customer")).thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute())
                .verifyComplete();

        verify(eventStore, times(1)).findAllAggregate("customer");
    }

    @Test
    void shouldPropagateErrorWhenEventStoreFails() {
        when(eventStore.findAllAggregate("customer"))
                .thenReturn(Flux.error(new RuntimeException("Event store error")));

        StepVerifier.create(useCase.execute())
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Event store error"))
                .verify();

        verify(eventStore, times(1)).findAllAggregate("customer");
    }
}
