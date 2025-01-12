package ec.com.sofka.config;

import ec.com.sofka.appservice.gateway.IPasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordAdapter implements IPasswordHasher {
    private final PasswordEncoder passwordEncoder;

    public PasswordAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }
}
