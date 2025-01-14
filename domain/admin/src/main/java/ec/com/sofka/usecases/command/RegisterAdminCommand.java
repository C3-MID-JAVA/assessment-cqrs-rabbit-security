package ec.com.sofka.usecases.command;

import ec.com.sofka.ROLE;

public class RegisterAdminCommand {
    private final String email;
    private final String password;
    private final ROLE role;

    public RegisterAdminCommand(String email, String password, ROLE role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ROLE getRole() {
        return role;
    }
}
