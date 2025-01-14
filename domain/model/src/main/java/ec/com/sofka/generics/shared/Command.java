package ec.com.sofka.generics.shared;

public abstract class Command {
    private final String aggregateId;

    protected Command(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateId() {
        return aggregateId;
    }
}
