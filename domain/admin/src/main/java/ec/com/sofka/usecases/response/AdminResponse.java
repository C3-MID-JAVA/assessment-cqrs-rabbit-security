package ec.com.sofka.usecases.response;

public class AdminResponse {
    private final String id;
    private final String email;
    private final String token;

    public AdminResponse(String id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}