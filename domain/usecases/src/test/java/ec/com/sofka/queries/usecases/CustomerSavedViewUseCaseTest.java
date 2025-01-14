package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.dto.CustomerDTO;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerSavedViewUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerSavedViewUseCase customerSavedViewUseCase;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO("John", "Doe", "1234567890", StatusEnum.ACTIVE);
    }

    @Test
    @DisplayName("should save customer when accept is called")
    void shouldSaveCustomer_WhenAcceptIsCalled() {
        when(customerRepository.save(customerDTO)).thenReturn(Mono.just(customerDTO));

        customerSavedViewUseCase.accept(customerDTO);

        verify(customerRepository, times(1)).save(customerDTO);
    }

}