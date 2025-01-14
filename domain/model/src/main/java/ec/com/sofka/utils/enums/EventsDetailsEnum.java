package ec.com.sofka.utils.enums;

import java.util.Arrays;

public enum EventsDetailsEnum {

    USER_CREATED(
            "USER_CREATED",
            "user.created.exchange",
            "user.created.queue",
            "user.created.routingKey"
    ),
    USER_UPDATED(
            "USER_UPDATED",
            "user.updated.exchange",
            "user.updated.queue",
            "user.updated.routingKey"
    ),
    CUSTOMER_CREATED(
            "CUSTOMER_CREATED",
            "customer.created.exchange",
            "customer.created.queue",
            "customer.created.routingKey"
    ),
    TRANSACTION_TYPE_CREATED(
            "TRANSACTION_TYPE_CREATED",
            "transactionType.created.exchange",
            "transactionType.created.queue",
            "transactionType.created.routingKey"
    ),
    ACCOUNT_CREATED(
            "ACCOUNT_CREATED",
            "account.created.exchange",
            "account.created.queue",
            "account.created.routingKey"
    ),
    ACCOUNT_UPDATED(
            "ACCOUNT_UPDATED",
            "account.updated.exchange",
            "account.updated.queue",
            "account.updated.routingKey"
    ),
    TRANSACTION_CREATED(
            "TRANSACTION_CREATED",
            "transaction.created.exchange",
            "transaction.created.queue",
            "transaction.created.routingKey"
    );

    private final String eventType;
    private final String exchange;
    private final String queue;
    private final String routingKey;

    EventsDetailsEnum(String eventType, String exchange, String queue, String routingKey) {
        this.eventType = eventType;
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    public static EventsDetailsEnum findByEventType(String eventType) {
        return Arrays.stream(EventsDetailsEnum.values())
                .filter(event -> event.getEventType().equals(eventType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Event type not found: " + eventType));
    }

    public String getEventType() {
        return eventType;
    }

    public String getExchange() {
        return exchange;
    }

    public String getQueue() {
        return queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }

}