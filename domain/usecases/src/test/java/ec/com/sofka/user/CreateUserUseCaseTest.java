package ec.com.sofka.user;

import ec.com.sofka.exception.ConflictException;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.user.request.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IEventStore repository;

    private CreateUserUseCase useCase;

    private BusEvent busEvent;
    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(repository, userRepository,busEvent);
    }

    @Test
    void shouldThrowConflictExceptionWhenDocumentIdAlreadyExists() {
        String documentId = "DOC123";
        String name = "John Doe";

        UserDTO existingUser = new UserDTO("user-1", name, documentId);
        when(userRepository.findByDocumentId(documentId)).thenReturn(Mono.just(existingUser));

        CreateUserRequest request = new CreateUserRequest(name, documentId);

        StepVerifier.create(useCase.execute(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ConflictException &&
                                throwable.getMessage().equals("User with document ID already exists"))
                .verify();

        verify(userRepository, times(1)).findByDocumentId(documentId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(repository);
    }

    @Test
    void shouldCreateUserWhenDocumentIdDoesNotExist() {
        String documentId = "DOC123";
        String name = "John Doe";

        when(userRepository.findByDocumentId(documentId)).thenReturn(Mono.empty());
        when(userRepository.save(any(UserDTO.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(repository.save(any(DomainEvent.class))).thenReturn(Mono.empty());

        CreateUserRequest request = new CreateUserRequest(name, documentId);

        StepVerifier.create(useCase.execute(request))
                .consumeNextWith(response -> {
                    assertNotNull(response.getCustomerId());
                    assertEquals(name, response.getName());
                    assertEquals(documentId, response.getDocumentId());
                })
                .verifyComplete();

        verify(userRepository, times(1)).findByDocumentId(documentId);
        verify(userRepository, times(1)).save(any(UserDTO.class));
        verify(repository, atLeastOnce()).save(any(DomainEvent.class));
    }

}
