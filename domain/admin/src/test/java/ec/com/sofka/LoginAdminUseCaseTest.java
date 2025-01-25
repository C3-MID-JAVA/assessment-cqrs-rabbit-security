package ec.com.sofka;

import ec.com.sofka.gateway.AdminRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.LoginAdminUseCase;
import ec.com.sofka.usecases.command.LoginAdminCommand;
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
public class LoginAdminUseCaseTest {
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginAdminUseCase useCase;

    @Test
    void shouldThrowNotFoundExceptionWhenAdminNotFound() {
        String email = "admin@test.com";
        String password = "password123";

        when(adminRepository.findByEmail(email)).thenReturn(Mono.empty());

        LoginAdminCommand command = new LoginAdminCommand(email, password);

        StepVerifier.create(useCase.execute(command))
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("Admin not found"))
                .verify();

        verify(adminRepository, times(1)).findByEmail(email);
        verifyNoInteractions(passwordHasher, jwtService);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenPasswordIsInvalid() {
        String email = "admin@test.com";
        String password = "wrongPassword";
        String hashedPassword = "hashedPassword";

        AdminDTO adminDTO = new AdminDTO("admin-1", email, hashedPassword, ROLE.GOD);

        when(adminRepository.findByEmail(email)).thenReturn(Mono.just(adminDTO));
        when(passwordHasher.verifyPassword(password, hashedPassword)).thenReturn(false);

        LoginAdminCommand command = new LoginAdminCommand(email, password);

        StepVerifier.create(useCase.execute(command))
                .expectErrorMatches(throwable ->
                        throwable instanceof AccessDeniedException &&
                                throwable.getMessage().equals("Bad credentials"))
                .verify();

        verify(adminRepository, times(1)).findByEmail(email);
        verify(passwordHasher, times(1)).verifyPassword(password, hashedPassword);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldReturnAdminResponseWhenLoginIsSuccessful() {
        String email = "admin@test.com";
        String password = "password123";
        String hashedPassword = "hashedPassword";
        String token = "jwtToken";
        String role = "GOD";

        AdminDTO adminDTO = new AdminDTO("admin-1", email, hashedPassword, ROLE.GOD);

        when(adminRepository.findByEmail(email)).thenReturn(Mono.just(adminDTO));
        when(passwordHasher.verifyPassword(password, hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(email, role)).thenReturn(token);

        LoginAdminCommand command = new LoginAdminCommand(email, password);

        StepVerifier.create(useCase.execute(command))
                .consumeNextWith(response -> {
                    assertNotNull(response);
                    assertEquals("admin-1", response.getId());
                    assertEquals(email, response.getEmail());
                    assertEquals(token, response.getToken());
                })
                .verifyComplete();

        verify(adminRepository, times(1)).findByEmail(email);
        verify(passwordHasher, times(1)).verifyPassword(password, hashedPassword);
        verify(jwtService, times(1)).generateToken(email, role);
    }
}
