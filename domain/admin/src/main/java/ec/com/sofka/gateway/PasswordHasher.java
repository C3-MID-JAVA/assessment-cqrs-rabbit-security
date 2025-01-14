package ec.com.sofka.gateway;

import ec.com.sofka.usecases.command.LoginAdminCommand;
import reactor.core.publisher.Mono;

public interface PasswordHasher {
    String hashPassword(String password);
    boolean verifyPassword(String rawPassword, String hashedPassword);
}
