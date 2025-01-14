package ec.com.sofka.usecases.commands;

import ec.com.sofka.generics.utils.Command;

public class CreateUserCommand extends Command {

    private final String username;
    private final String password;
    private final String roles;

    public CreateUserCommand(String username, String password, String roles) {
        super(null);
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
