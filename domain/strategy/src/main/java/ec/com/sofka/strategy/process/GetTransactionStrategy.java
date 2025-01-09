package ec.com.sofka.strategy.process;

import ec.com.sofka.account.Account;
import ec.com.sofka.enums.OperationType;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.strategy.TransaccionStrategy;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class GetTransactionStrategy {

    private final TransaccionStrategyFactory factory;

    public GetTransactionStrategy(TransaccionStrategyFactory factory) {
        this.factory = factory;
    }

    public Mono<TransaccionStrategy> apply(Account account, TransactionType type, OperationType operationType, BigDecimal amount) {
        TransaccionStrategy strategy = factory.getStrategy(type, operationType);
        strategy.validate(account, amount);
        return Mono.just(strategy);
    }
}
