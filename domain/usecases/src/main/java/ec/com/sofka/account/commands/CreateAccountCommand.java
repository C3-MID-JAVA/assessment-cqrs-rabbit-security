package ec.com.sofka.account.commands;

import ec.com.sofka.generics.utils.Command;

public class CreateAccountCommand extends Command {
    private final String userId;

    public CreateAccountCommand(String userId) {
        super(null);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
