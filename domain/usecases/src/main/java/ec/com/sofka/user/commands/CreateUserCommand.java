package ec.com.sofka.user.commands;

import ec.com.sofka.generics.utils.Command;

public class CreateUserCommand extends Command {
    private final String name;
    private final String documentId;

    public CreateUserCommand(String name, String documentId) {
        super(null);
        this.name = name;
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public String getDocumentId() {
        return documentId;
    }
}
