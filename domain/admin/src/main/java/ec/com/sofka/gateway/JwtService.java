package ec.com.sofka.gateway;

public interface JwtService {
    String generateToken(String username, String role);
}