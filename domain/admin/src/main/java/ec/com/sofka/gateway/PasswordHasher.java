package ec.com.sofka.gateway;

public interface PasswordHasher {
    String hashPassword(String password);
    boolean verifyPassword(String rawPassword, String hashedPassword);
}