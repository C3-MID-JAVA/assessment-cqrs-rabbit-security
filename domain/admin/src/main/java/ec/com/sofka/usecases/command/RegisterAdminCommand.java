package ec.com.sofka.usecases.command;

import ec.com.sofka.RoleEnum;

public class RegisterAdminCommand {
    private final String email;
    private final String password;
    private final RoleEnum role;

    public RegisterAdminCommand(String email, String password, RoleEnum role) {
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

    public RoleEnum getRole() {
        return role;
    }
}