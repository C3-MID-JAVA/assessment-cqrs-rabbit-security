package ec.com.sofka.usecases.queries;

public class RegisterResponse {
    private String email;

    public RegisterResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
