package ec.com.sofka.user.commands;

import ec.com.sofka.ConflictException;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.user.commands.usecases.CreateUserUseCase;
import ec.com.sofka.user.queries.query.GetUserByDocumentQuery;
import ec.com.sofka.user.queries.usecases.GetUserByDocumentViewUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private BusEvent busEvent;


    @Mock
    private GetUserByDocumentViewUseCase getUserByDocumentViewUseCase;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    void shouldThrowConflictExceptionWhenDocumentIdAlreadyExists() {
        String documentId = "DOC123";
        String name = "John Doe";

        UserDTO existingUser = new UserDTO("user-1", name, documentId);

        when(getUserByDocumentViewUseCase.get(any(GetUserByDocumentQuery.class)))
                .thenReturn(Mono.just(existingUser));

        CreateUserCommand request = new CreateUserCommand(name, documentId);

        StepVerifier.create(useCase.execute(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ConflictException &&
                                throwable.getMessage().equals("User with document ID already exists"))
                .verify();

        verify(getUserByDocumentViewUseCase, times(1)).get(any(GetUserByDocumentQuery.class));
        verifyNoInteractions(repository);
    }

    @Test
    void shouldCreateUserWhenDocumentIdDoesNotExist() {
        String documentId = "DOC123";
        String name = "John Doe";

        when(getUserByDocumentViewUseCase.get(any(GetUserByDocumentQuery.class)))
                .thenReturn(Mono.empty());

        when(repository.save(any(DomainEvent.class))).thenReturn(Mono.empty());

        CreateUserCommand request = new CreateUserCommand(name, documentId);

        StepVerifier.create(useCase.execute(request))
                .consumeNextWith(response -> {
                    assertNotNull(response.getId());
                    assertEquals(name, response.getName());
                    assertEquals(documentId, response.getDocumentId());
                })
                .verifyComplete();

        verify(getUserByDocumentViewUseCase, times(1)).get(any(GetUserByDocumentQuery.class));
        verify(repository, atLeastOnce()).save(any(DomainEvent.class));
    }

}
