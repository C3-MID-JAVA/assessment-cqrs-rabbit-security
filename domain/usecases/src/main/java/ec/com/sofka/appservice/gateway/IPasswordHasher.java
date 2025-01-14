package ec.com.sofka.appservice.gateway;

public interface IPasswordHasher {
    String hash(String password);
}
