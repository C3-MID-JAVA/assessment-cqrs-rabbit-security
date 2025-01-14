package ec.com.sofka.mapper;

import ec.com.sofka.data.transaction.*;
import ec.com.sofka.gateway.dto.transaction.*;

import java.util.Map;
import java.util.function.Function;

public class TransactionMapper {

    public static TransactionDTO toTransactionDTO(TransactionEntity transactionEntity) {
        return switch (transactionEntity.getTransactionType()) {
            case ConstansTrType.ATM -> {
                AtmTransactionEntity atmDeposit = (AtmTransactionEntity) transactionEntity;
                yield new AtmTransactionDTO(
                        transactionEntity.getId(),
                        null,
                        transactionEntity.getDescription(),
                        transactionEntity.getAmount(),
                        transactionEntity.getTransactionType(),
                        transactionEntity.getTransactionFee(),
                        transactionEntity.getTimestamp(),
                        AccountMapper.toDTO(transactionEntity.getAccount()),
                        CardMapper.toCardDTO(transactionEntity.getCard()),
                        atmDeposit.getAtmName(),
                        atmDeposit.getOperationType());
            }
            case ConstansTrType.BETWEEN_ACCOUNT -> {
                AccountDepositEntity accountDeposit = (AccountDepositEntity) transactionEntity;
                yield new AccountDepositDTO(
                        transactionEntity.getId(),
                        null,
                        transactionEntity.getDescription(),
                        transactionEntity.getAmount(),
                        transactionEntity.getTransactionType(),
                        transactionEntity.getTransactionFee(),
                        transactionEntity.getTimestamp(),
                        AccountMapper.toDTO(transactionEntity.getAccount()),
                        CardMapper.toCardDTO(transactionEntity.getCard()),
                        AccountMapper.toDTO(accountDeposit.getAccountReceiver()),
                        null);
            }
            case ConstansTrType.STORE_PURCHASE -> {
                PaymentStoreTransactionEntity paymentStore = (PaymentStoreTransactionEntity) transactionEntity;
                yield new PaymentStoreDTO(
                        transactionEntity.getId(),
                        null,
                        transactionEntity.getDescription(),
                        transactionEntity.getAmount(),
                        transactionEntity.getTransactionType(),
                        transactionEntity.getTransactionFee(),
                        transactionEntity.getTimestamp(),
                        AccountMapper.toDTO(transactionEntity.getAccount()),
                        CardMapper.toCardDTO(transactionEntity.getCard()),
                        paymentStore.getMarketName());
            }
            case ConstansTrType.WEB_PURCHASE -> {
                PaymentWebTransactionEntity paymentWeb = (PaymentWebTransactionEntity) transactionEntity;
                yield new PaymentWebDTO(
                        transactionEntity.getId(),
                        null,
                        transactionEntity.getDescription(),
                        transactionEntity.getAmount(),
                        transactionEntity.getTransactionType(),
                        transactionEntity.getTransactionFee(),
                        transactionEntity.getTimestamp(),
                        AccountMapper.toDTO(transactionEntity.getAccount()),
                        CardMapper.toCardDTO(transactionEntity.getCard()),
                        paymentWeb.getWebsite());
            }
            case ConstansTrType.BRANCH_DEPOSIT -> {
                BranchDepositEntity branchDeposit = (BranchDepositEntity) transactionEntity;
                yield new BranchDepositDTO(
                        transactionEntity.getId(),
                        null,
                        transactionEntity.getDescription(),
                        transactionEntity.getAmount(),
                        transactionEntity.getTransactionType(),
                        transactionEntity.getTransactionFee(),
                        transactionEntity.getTimestamp(),
                        AccountMapper.toDTO(transactionEntity.getAccount()),
                        CardMapper.toCardDTO(transactionEntity.getCard()),
                        branchDeposit.getBranchName());
            }
            default -> throw new RuntimeException("Unsupported transaction type in Mapper");
        };
    }

    public static TransactionEntity toTransactionEntity(TransactionDTO transaction) {

        if (transaction == null) return null;
        return MAPPERS.getOrDefault(transaction.getClass(), defaultTransactionMapper())
                .apply(transaction);
    }

    private static final Map<Class<? extends TransactionDTO>, Function<TransactionDTO, TransactionEntity>> MAPPERS = Map.of(
            AtmTransactionDTO.class, transaction -> {
                AtmTransactionDTO atmTransaction = (AtmTransactionDTO) transaction;
                return new AtmTransactionEntity(
                        transaction.getId(),
                        atmTransaction.getDescription(),
                        atmTransaction.getAmount(),
                        atmTransaction.getTransactionType(),
                        atmTransaction.getTransactionFee(),
                        null,
                        AccountMapper.toEntity(atmTransaction.getAccount()),
                        CardMapper.toCardEntity(atmTransaction.getCard()),
                        atmTransaction.getAtmName(),
                        atmTransaction.getOperationType()
                );
            },
            AccountDepositDTO.class, transaction -> {
                AccountDepositDTO accountDeposit = (AccountDepositDTO) transaction;
                return new AccountDepositEntity(
                        transaction.getId(),
                        accountDeposit.getDescription(),
                        accountDeposit.getAmount(),
                        accountDeposit.getTransactionType(),
                        accountDeposit.getTransactionFee(),
                        null,
                        AccountMapper.toEntity(accountDeposit.getAccount()),
                        CardMapper.toCardEntity(accountDeposit.getCard()),
                        AccountMapper.toEntity(accountDeposit.getAccountReceiver())
                );
            },
            BranchDepositDTO.class, transaction -> {
                BranchDepositDTO branchDepositDTO = (BranchDepositDTO) transaction;
                return new BranchDepositEntity(
                        transaction.getId(),
                        branchDepositDTO.getDescription(),
                        branchDepositDTO.getAmount(),
                        branchDepositDTO.getTransactionType(),
                        branchDepositDTO.getTransactionFee(),
                        null,
                        AccountMapper.toEntity(branchDepositDTO.getAccount()),
                        CardMapper.toCardEntity(branchDepositDTO.getCard()),
                        branchDepositDTO.getBranchName()
                );
            },
            PaymentStoreDTO.class, transaction -> {
                PaymentStoreDTO transactionDTO = (PaymentStoreDTO) transaction;
                return new PaymentStoreTransactionEntity(
                        transaction.getId(),
                        transactionDTO.getDescription(),
                        transactionDTO.getAmount(),
                        transactionDTO.getTransactionType(),
                        transactionDTO.getTransactionFee(),
                        null,
                        AccountMapper.toEntity(transactionDTO.getAccount()),
                        CardMapper.toCardEntity(transactionDTO.getCard()),
                        transactionDTO.getMarketName()
                );
            },
            PaymentWebDTO.class, transaction -> {
                PaymentWebDTO transactionDTO = (PaymentWebDTO) transaction;
                return new PaymentWebTransactionEntity(
                        transaction.getId(),
                        transactionDTO.getDescription(),
                        transactionDTO.getAmount(),
                        transactionDTO.getTransactionType(),
                        transactionDTO.getTransactionFee(),
                        null,
                        AccountMapper.toEntity(transactionDTO.getAccount()),
                        CardMapper.toCardEntity(transactionDTO.getCard()),
                        transactionDTO.getWebsite()
                );
            }

    );

    private static Function<TransactionDTO, TransactionEntity> defaultTransactionMapper() {
        return transaction -> new TransactionEntity(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionFee(),
                null,
                AccountMapper.toEntity(transaction.getAccount()),
                CardMapper.toCardEntity(transaction.getCard())
        );
    }
}
