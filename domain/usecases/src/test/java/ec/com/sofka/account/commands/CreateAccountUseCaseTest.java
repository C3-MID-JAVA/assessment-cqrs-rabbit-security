package ec.com.sofka.account.commands;

import ec.com.sofka.NotFoundException;
import ec.com.sofka.account.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @Mock
    private BusEvent busEvent;

    @InjectMocks
    private CreateAccountUseCase useCase;

    @Test
    void shouldSuccessfullyCreateAccount() {
        String customerIdentifier = "customer123";
        String adminUserId = "admin123";

        UserCreated userCreationEvent = new UserCreated(adminUserId, "Jane Doe", "DOC456");
        userCreationEvent.setAggregateRootId(customerIdentifier);

        // Simulamos la obtención de eventos previos
        when(eventStore.findAggregate(customerIdentifier, "customer"))
                .thenReturn(Flux.just(userCreationEvent));

        // Simulamos la persistencia de un nuevo evento
        when(eventStore.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        // Creamos el comando para crear la cuenta
        CreateAccountCommand accountCreationCommand = new CreateAccountCommand(customerIdentifier);

        StepVerifier.create(useCase.execute(accountCreationCommand))
                .consumeNextWith(accountDetails -> {
                    Assertions.assertNotNull(accountDetails, "La respuesta no debe ser nula.");
                    Assertions.assertEquals(customerIdentifier, accountDetails.getCustomerId(), "El ID del cliente no coincide.");
                    Assertions.assertTrue(accountDetails.getAccountNumber().length() == 8, "El número de cuenta debe tener 8 caracteres.");
                    Assertions.assertEquals(BigDecimal.ZERO, accountDetails.getBalance(), "El saldo debe ser cero.");
                    Assertions.assertEquals(adminUserId, accountDetails.getUserId(), "El ID del usuario no coincide.");
                })
                .verifyComplete();

        // Verificamos las interacciones con el almacenamiento de eventos
        verify(eventStore, times(1)).findAggregate(customerIdentifier, "customer");
        verify(eventStore, times(1)).save(any(DomainEvent.class));  // Verificamos que se haya guardado un evento
    }

    @Test
    void shouldThrowNotFoundErrorWhenUserDoesNotExist() {
        String customerIdentifier = "customer123";

        // Simulamos que no se encuentra al usuario
        when(eventStore.findAggregate(customerIdentifier, "customer"))
                .thenReturn(Flux.empty());

        CreateAccountCommand accountCreationCommand = new CreateAccountCommand(customerIdentifier);

        // Verificamos que se lance la excepción NotFoundException
        StepVerifier.create(useCase.execute(accountCreationCommand))
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("User not found"))
                .verify();

        // Verificamos que se consultó el almacenamiento
        verify(eventStore, times(1)).findAggregate(customerIdentifier, "customer");
    }

}

