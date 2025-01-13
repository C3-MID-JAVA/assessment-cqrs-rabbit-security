package ec.com.sofka.usecases.queries;

public class AuthenticationResponse {
    private  String id;
    private String  email;
    private String token;

    public AuthenticationResponse(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public AuthenticationResponse(String id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }

    public AuthenticationResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
