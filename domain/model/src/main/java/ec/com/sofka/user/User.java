package ec.com.sofka.user;

import ec.com.sofka.generics.utils.Entity;
import ec.com.sofka.user.values.objects.DocumentId;
import ec.com.sofka.user.values.objects.Name;
import ec.com.sofka.user.values.UserId;

public class User extends Entity<UserId> {
    private final Name name;
    private final DocumentId documentId;

    public User(UserId id, Name name, DocumentId documentId) {
        super(id);
        this.name = name;
        this.documentId = documentId;
    }

    public Name getName() {
        return name;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }
}
