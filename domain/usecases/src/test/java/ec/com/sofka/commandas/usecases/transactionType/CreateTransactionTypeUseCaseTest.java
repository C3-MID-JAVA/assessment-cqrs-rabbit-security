package ec.com.sofka.commandas.usecases.transactionType;

import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.aggregates.account.entities.transactionType.values.TransactionTypeId;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.commands.TransactionTypeCommand;
import ec.com.sofka.commands.usecases.transactionType.CreateTransactionTypeUseCase;
import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CreateTransactionTypeUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private TransactionTypeRepository transactionTypeRepository;

    @Mock
    private ErrorBusMessage errorBusMessage;

    @Mock
    private EventBusMessage eventBusMessage;

    @InjectMocks
    private CreateTransactionTypeUseCase useCase;

    @Test
    void createTransactionTypeSuccessfully() {
        TransactionTypeCommand command = new TransactionTypeCommand(
                "TRANSFER",
                "Transfer money",
                new BigDecimal("100.0"),
                true,
                true,
                StatusEnum.ACTIVE
        );

        when(transactionTypeRepository.findByType(command.getType()))
                .thenReturn(Mono.empty());

        when(repository.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response ->
                        response.getType().equals("TRANSFER") &&
                                response.getDescription().equals("Transfer money") &&
                                response.getValue().equals(new BigDecimal("100.0")) &&
                                response.getTransactionCost().equals(true) &&
                                response.getDiscount().equals(true) &&
                                response.getStatus().name().equals("ACTIVE"))
                .verifyComplete();

        verify(transactionTypeRepository).findByType("TRANSFER");
        verify(repository, times(1)).save(any(DomainEvent.class));
        verify(errorBusMessage, never()).sendMsg(any());
    }

    @Test
    void createTransactionTypeFailsWhenTypeAlreadyExists() {
        TransactionTypeCommand command = new TransactionTypeCommand(
                "TRANSFER",
                "Transfer money",
                new BigDecimal("100.0"),
                true,
                true,
                StatusEnum.ACTIVE
        );

        TransactionType existingType = new TransactionType(
                TransactionTypeId.of("123"),
                "TRANSFER",
                "Transfer money",
                Amount.of(new BigDecimal("100.0")),
                true,
                true,
                StatusEnum.ACTIVE
        );

        when(transactionTypeRepository.findByType(command.getType()))
                .thenReturn(Mono.just(TransactionTypeMapper.mapToDTOFromModel(existingType)));

        StepVerifier.create(useCase.execute(command))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("The transaction type is already registered."))
                .verify();

        verify(transactionTypeRepository).findByType("TRANSFER");
        verify(repository, never()).save(any());
        verify(eventBusMessage, never()).sendEvent(any());
    }

}