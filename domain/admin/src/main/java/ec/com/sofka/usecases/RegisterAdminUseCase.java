package ec.com.sofka.usecases;

import ec.com.sofka.AdminDTO;
import ec.com.sofka.ConflictException;
import ec.com.sofka.gateway.AdminRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.command.RegisterAdminCommand;
import ec.com.sofka.usecases.responses.AdminResponse;
import reactor.core.publisher.Mono;

public class RegisterAdminUseCase {
    private final AdminRepository adminRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public RegisterAdminUseCase(AdminRepository adminRepository, PasswordHasher passwordHasher, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }

    public Mono<AdminResponse> execute(RegisterAdminCommand registerAdminCommand) {
        return adminRepository.findByEmail(registerAdminCommand.getEmail())
                .flatMap(existingAdmin -> Mono.<AdminResponse>error(new ConflictException("Admin already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                            String hashedPassword = passwordHasher.hashPassword(registerAdminCommand.getPassword());
                            return adminRepository.save(new AdminDTO(registerAdminCommand.getEmail(), hashedPassword, registerAdminCommand.getRole()))
                                    .map(savedAdmin -> {
                                        String token = jwtService.generateToken(savedAdmin.getEmail(), savedAdmin.getRole().name());
                                        return new AdminResponse(savedAdmin.getId(), savedAdmin.getEmail(), token);
                                    });
                        })
                );
    }
}
