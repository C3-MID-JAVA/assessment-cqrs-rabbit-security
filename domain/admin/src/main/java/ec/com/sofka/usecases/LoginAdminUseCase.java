package ec.com.sofka.usecases;

import ec.com.sofka.AccessDeniedException;
import ec.com.sofka.NotFoundException;
import ec.com.sofka.gateway.AdminRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.command.LoginAdminCommand;
import ec.com.sofka.usecases.responses.AdminResponse;
import reactor.core.publisher.Mono;

public class LoginAdminUseCase {
    private final AdminRepository adminRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public LoginAdminUseCase(AdminRepository adminRepository, PasswordHasher passwordHasher, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }

    public Mono<AdminResponse> execute(LoginAdminCommand loginAdminCommand) {
        return adminRepository.findByEmail(loginAdminCommand.getEmail())
                .switchIfEmpty(Mono.error(new NotFoundException("Admin not found")))
                .map(adminDTO -> {
                    if (!passwordHasher.verifyPassword(loginAdminCommand.getPassword(), adminDTO.getPassword())) {
                        throw new AccessDeniedException("Bad credentials");
                    }
                    String token = jwtService.generateToken(adminDTO.getEmail(), adminDTO.getRole().name());
                    return new AdminResponse(adminDTO.getId(), adminDTO.getEmail(), token);
                });
    }

}
