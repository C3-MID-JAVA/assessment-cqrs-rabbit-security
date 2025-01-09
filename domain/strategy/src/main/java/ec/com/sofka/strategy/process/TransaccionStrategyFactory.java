package ec.com.sofka.strategy.process;

import ec.com.sofka.ConflictException;
import ec.com.sofka.enums.OperationType;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.strategy.*;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class TransaccionStrategyFactory {

    private final Map<TransactionType, TransaccionStrategy> strategies = new EnumMap<>(TransactionType.class);

    public TransaccionStrategyFactory() {
        strategies.put(TransactionType.ATM_DEPOSIT, new ATMDepositStrategy());
        strategies.put(TransactionType.OTHER_ACCOUNT_DEPOSIT, new OtherAccountDepositStrategy());
        strategies.put(TransactionType.BRANCH_DEPOSIT, new BranchDepositStrategy());
        strategies.put(TransactionType.ATM_WITHDRAWAL, new ATMWithDrawalStrategy());
        strategies.put(TransactionType.ONLINE_PURCHASE, new OnlinePurchaseStrategy());
        strategies.put(TransactionType.PHYSICAL_PURCHASE, new PhysicalPurchaseStrategy());
    }

    public TransaccionStrategy getStrategy(TransactionType tipoTransaccion, OperationType operationType) {
        if (operationType == OperationType.DEPOSIT) {
            if (esTransaccionValidaDeposito(tipoTransaccion)) {
                return strategies.get(tipoTransaccion);
            }
        } else if (operationType == OperationType.WITHDRAWAL) {
            if (esTransaccionValidaRetiro(tipoTransaccion)) {
                return strategies.get(tipoTransaccion);
            }
        }

        throw new ConflictException("Invalid transaction type for operation " + operationType);
    }

    private boolean esTransaccionValidaDeposito(TransactionType transactionType) {
        return transactionType == TransactionType.ATM_DEPOSIT ||
                transactionType == TransactionType.OTHER_ACCOUNT_DEPOSIT ||
                transactionType == TransactionType.BRANCH_DEPOSIT;
    }

    private boolean esTransaccionValidaRetiro(TransactionType transactionType) {
        return transactionType == TransactionType.ATM_WITHDRAWAL ||
                transactionType == TransactionType.ONLINE_PURCHASE ||
                transactionType == TransactionType.PHYSICAL_PURCHASE;
    }
}
