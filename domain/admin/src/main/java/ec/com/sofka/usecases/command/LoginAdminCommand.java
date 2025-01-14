package ec.com.sofka.usecases.command;

public class LoginAdminCommand {
    private final String email;
    private final String password;

    public LoginAdminCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
