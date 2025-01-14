package ec.com.sofka;

import ec.com.sofka.transaction.TransactionType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class TransactionStrategyFactory {

    private final Map<TransactionType, TransactionStrategy> strategies;

    public TransactionStrategyFactory(Map<String, TransactionStrategy> strategyBeans) {
        strategies = new EnumMap<>(TransactionType.class);
        strategies.put(TransactionType.ATM_DEPOSIT, strategyBeans.get("aTMDepositStrategy"));
        strategies.put(TransactionType.ATM_WITHDRAWAL, strategyBeans.get("aTMWithdrawalStrategy"));
        strategies.put(TransactionType.BRANCH_DEPOSIT, strategyBeans.get("branchDepositStrategy"));
        strategies.put(TransactionType.PHYSICAL_PURCHASE, strategyBeans.get("physicalPurchase"));
        strategies.put(TransactionType.TRANSFER_DEPOSIT, strategyBeans.get("transferDepositStrategy"));
        strategies.put(TransactionType.WEB_PURCHASE, strategyBeans.get("webPurchaseStrategy"));
    }

    public TransactionStrategy getStrategy(TransactionType type){
        return strategies.get(type);
    }
}
