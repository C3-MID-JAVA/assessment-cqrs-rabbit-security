package ec.com.sofka.data;

import ec.com.sofka.ConstansTrType;
import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.transaction.Payment;
import ec.com.sofka.aggregate.transaction.events.TransactionCreated;
import ec.com.sofka.card.Card;
import ec.com.sofka.commands.transaction.*;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.transaction.*;
import ec.com.sofka.transaction.*;
import ec.com.sofka.transaction.values.TransactionId;

import java.time.LocalDateTime;

public class TransactionUtils {
    public static TransactionCreated createTransactionCreated(TransactionCommand cmd, Account account, Card card) {
        switch (cmd.getTransactionType()) {
            case "ATM":
                AtmTransactionCommand atmTransactionDTO = (AtmTransactionCommand) cmd;
                return new TransactionCreated(
                        new TransactionId().getValue(),
                        atmTransactionDTO.getDescription(),
                        atmTransactionDTO.getAmount(),
                        atmTransactionDTO.getTransactionType(),
                        atmTransactionDTO.getTransactionFee(),
                        LocalDateTime.now(),
                        account,
                        card,
                        atmTransactionDTO.getAtmName(),
                        atmTransactionDTO.getOperationType(),
                        null,
                        null,
                        null,
                        null
                );
            case "WP":
                PaymentWebCommand paymentWebDTO = (PaymentWebCommand) cmd;
                return new TransactionCreated(
                        new TransactionId().getValue(),
                        paymentWebDTO.getDescription(),
                        paymentWebDTO.getAmount(),
                        paymentWebDTO.getTransactionType(),
                        paymentWebDTO.getTransactionFee(),
                        LocalDateTime.now(),
                        account,
                        card,
                        null,
                        null,
                        null,
                        null,
                        paymentWebDTO.getWebsite(),
                        null
                );
            case "SP":
                PaymentStoreCommand paymentStoreDTO = (PaymentStoreCommand) cmd;
                return new TransactionCreated(
                        new TransactionId().getValue(),
                        paymentStoreDTO.getDescription(),
                        paymentStoreDTO.getAmount(),
                        paymentStoreDTO.getTransactionType(),
                        paymentStoreDTO.getTransactionFee(),
                        LocalDateTime.now(),
                        account,
                        card,
                        null,
                        null,
                        null,
                        paymentStoreDTO.getMarketName(),
                        null,
                        null
                );
            case "BD":
                BranchDepositCommand branchDepositDTO = (BranchDepositCommand) cmd;
                return new TransactionCreated(
                        new TransactionId().getValue(),
                        branchDepositDTO.getDescription(),
                        branchDepositDTO.getAmount(),
                        branchDepositDTO.getTransactionType(),
                        branchDepositDTO.getTransactionFee(),
                        LocalDateTime.now(),
                        account,
                        card,
                        null,
                        null,
                        null,
                        null,
                        null,
                        branchDepositDTO.getBranchName()
                );
            default:
                throw new IllegalArgumentException("Unsupported transaction type: " + cmd.getClass().getName());
        }


    }

    public static TransactionDTO generateResponse(Payment<? extends Transaction> payment) {
        return switch (payment.getTransaction().getTransactionType().getValue()) {
            case ConstansTrType.ATM -> {
                AtmTransaction atmTransaction = (AtmTransaction) payment.getTransaction();
                yield new AtmTransactionDTO(
                        null,
                        payment.getId().getValue(),
                        payment.getTransaction().getDescription().getValue(),
                        payment.getTransaction().getAmount().getValue(),
                        payment.getTransaction().getTransactionType().getValue(),
                        payment.getTransaction().getTransactionFee().getValue(),
                        payment.getTransaction().getTimestamp().getValue(),
                        null,
                        null,
                        atmTransaction.getAtmName().getValue(),
                        atmTransaction.getOperationType().getValue());
            }
            case ConstansTrType.BETWEEN_ACCOUNT -> {
                AccountDeposit accountDeposit = (AccountDeposit) payment.getTransaction();
                yield new AccountDepositDTO(
                        payment.getId().getValue(),
                        null,
                        payment.getTransaction().getDescription().getValue(),
                        payment.getTransaction().getAmount().getValue(),
                        payment.getTransaction().getTransactionType().getValue(),
                        payment.getTransaction().getTransactionFee().getValue(),
                        payment.getTransaction().getTimestamp().getValue(),
                        null,
                        null,
                        new AccountDTO(accountDeposit.getAccountReceiver().getValue().getBalance().getValue(),
                                accountDeposit.getAccountReceiver().getValue().getOwnerName().getValue(),
                                accountDeposit.getAccountReceiver().getValue().getAccountNumber().getValue(),
                                accountDeposit.getAccountReceiver().getValue().getType().getValue()),
                        null);
            }
            case ConstansTrType.STORE_PURCHASE -> {
                PaymentStoreTransaction paymentStore = (PaymentStoreTransaction) payment.getTransaction();
                yield new PaymentStoreDTO(
                        payment.getId().getValue(),
                        null,
                        payment.getTransaction().getDescription().getValue(),
                        payment.getTransaction().getAmount().getValue(),
                        payment.getTransaction().getTransactionType().getValue(),
                        payment.getTransaction().getTransactionFee().getValue(),
                        payment.getTransaction().getTimestamp().getValue(),
                        null,
                        null,
                        paymentStore.getMarketName().getValue());
            }
            case ConstansTrType.WEB_PURCHASE -> {
                PaymentWebTransaction paymentWeb = (PaymentWebTransaction) payment.getTransaction();
                yield new PaymentWebDTO(
                        payment.getId().getValue(),
                        null,
                        payment.getTransaction().getDescription().getValue(),
                        payment.getTransaction().getAmount().getValue(),
                        payment.getTransaction().getTransactionType().getValue(),
                        payment.getTransaction().getTransactionFee().getValue(),
                        payment.getTransaction().getTimestamp().getValue(),
                        null, null,
                        paymentWeb.getWebsite().getValue());
            }
            case ConstansTrType.BRANCH_DEPOSIT -> {
                BranchDeposit branchDeposit = (BranchDeposit) payment.getTransaction();
                yield new BranchDepositDTO(
                        payment.getId().getValue(),
                        null,
                        payment.getTransaction().getDescription().getValue(),
                        payment.getTransaction().getAmount().getValue(),
                        payment.getTransaction().getTransactionType().getValue(),
                        payment.getTransaction().getTransactionFee().getValue(),
                        payment.getTransaction().getTimestamp().getValue(),
                        null, null,
                        branchDeposit.getBranchName().getValue());
            }
            default -> throw new RuntimeException("Unsupported transaction type in Mapper");
        };
    }
}
