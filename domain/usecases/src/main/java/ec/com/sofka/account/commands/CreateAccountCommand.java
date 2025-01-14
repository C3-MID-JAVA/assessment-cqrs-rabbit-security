package ec.com.sofka.account.commands;

import ec.com.sofka.generics.utils.Command;

public class CreateAccountCommand extends Command {
    public CreateAccountCommand(String aggregateId) {
        super(aggregateId);
    }
}
