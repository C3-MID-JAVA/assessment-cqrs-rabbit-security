package ec.com.sofka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BusAdapter implements BusEvent {

    private final RabbitTemplate rabbitTemplate;

    public BusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendEventAccountCreated(Mono<DomainEvent> event) {
        event.subscribe(accountCreated -> {
                    if (!accountCreated.getEventType().equals("ACCOUNT_CREATED")) return;
                    rabbitTemplate.convertAndSend("account.created.exchange", "account.created.event", accountCreated);
                }
        );
    }

    @Override
    public void sendEventUserCreated(Mono<DomainEvent> event) {
        event.subscribe(userCreated -> {
            if (!userCreated.getEventType().equals("USER_CREATED")) return;
            rabbitTemplate.convertAndSend("user.created.exchange", "user.created.event", userCreated);
        });
    }

    public void sendEventTransactionCreated(Mono<DomainEvent> event) {
        event.subscribe(transactionCreated -> {
            if (!transactionCreated.getEventType().equals("TRANSACTION_CREATED")) return;
            rabbitTemplate.convertAndSend("transaction.created.exchange", "transaction.created.event", transactionCreated);
        });
    }

    @Override
    public void sendEventBalanceUpdated(Mono<DomainEvent> event) {
        event.subscribe(balanceUpdated -> {
            if (!balanceUpdated.getEventType().equals("ACCOUNT_BALANCE_UPDATED")) return;
            rabbitTemplate.convertAndSend("balance.updated.exchange", "balance.updated.event", balanceUpdated);
        });
    }

}
