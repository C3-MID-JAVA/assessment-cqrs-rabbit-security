package ec.com.sofka.aggregate.customer.events;

import ec.com.sofka.generics.domain.DomainEvent;

public class UserCreated extends DomainEvent {
    private String id;
    private String name;
    private String documentId;

    public UserCreated(String id, String name, String documentId) {
        super(EventsEnum.USER_CREATED.name());
        this.id = id;
        this.name = name;
        this.documentId = documentId;
    }

    public UserCreated(String name, String documentId) {
        super(EventsEnum.USER_CREATED.name());
        this.name = name;
        this.documentId = documentId;
    }

    public UserCreated() {
        super(EventsEnum.USER_CREATED.name());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocumentId() {
        return documentId;
    }
}
