package ec.com.sofka.usecases.queries.responses;

public class CreateUserResponse {

    private String username;
    private String password;
    private String roles;

    public CreateUserResponse(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }
}
