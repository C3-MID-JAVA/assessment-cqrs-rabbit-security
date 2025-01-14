package ec.com.sofka.config;


import ec.com.sofka.gateway.PasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordHasherConfig {

    @Bean
    public PasswordHasher passwordHasher() {
        return new PasswordHasher() {
            @Override
            public String hashPassword(String password) {
                // Implement your password hashing logic here
                return password; // Replace with actual hashing logic
            }

            @Override
            public boolean verifyPassword(String rawPassword, String hashedPassword) {
                // Implement your password verification logic here
                return rawPassword.equals(hashedPassword); // Replace with actual verification logic
            }
        };
    }
}