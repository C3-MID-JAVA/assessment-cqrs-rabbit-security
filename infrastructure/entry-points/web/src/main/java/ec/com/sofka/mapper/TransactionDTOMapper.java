package ec.com.sofka.mapper;

import ec.com.sofka.commands.transaction.*;
import ec.com.sofka.data.TransactionReqDTO;
import ec.com.sofka.gateway.dto.transaction.*;

public class TransactionDTOMapper {
    public static TransactionReqDTO toReqDTO(TransactionDTO transaction) {
        TransactionReqDTO transactionDto = new TransactionReqDTO(
                transaction.getCustomerId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionFee(),
                null,
                null,
                null);


        if (transaction instanceof AtmTransactionDTO atmTransaction) {
            System.out.println("ATM TRANSACTION" + transactionDto.getDescription() + " " + transaction.getDescription());
            transactionDto.setAtmName(atmTransaction.getAtmName());
            transactionDto.setOperationType(atmTransaction.getOperationType());
        }

        if (transaction instanceof AccountDepositDTO accountDeposit) {
            transactionDto.setAccountReceiver(AccountDTOMapper.toAccountReqDTO(accountDeposit.getAccountReceiver()));
            transactionDto.setReceiverCustomerId(accountDeposit.getReceiverCustomerId());
        }
        if(transaction instanceof BranchDepositDTO branchTransaction) {
            transactionDto.setBranchName(branchTransaction.getBranchName());
        }
        if(transaction instanceof PaymentStoreDTO paymentStoreTransaction) {
            transactionDto.setMarketName(paymentStoreTransaction.getMarketName());
        }

        if(transaction instanceof PaymentWebDTO paymentStoreTransaction) {
            transactionDto.setWebsite(paymentStoreTransaction.getWebsite());
        }
        return transactionDto;

    }

    public static TransactionDTO toTransactionDTO(TransactionReqDTO transactionReqDTO) {
        if (transactionReqDTO == null) return null;
        TransactionDTO transaction = null;
        transaction = switch (transactionReqDTO.getTransactionType()) {
            case "ATM" -> new AtmTransactionDTO(
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    AccountDTOMapper.toAccountDTO(transactionReqDTO.getAccount()),
                    CardDTOMapper.toCardDTO(transactionReqDTO.getCard()),
                    transactionReqDTO.getAtmName(),
                    transactionReqDTO.getOperationType()
            );
            case "BA" -> new AccountDepositDTO(
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    AccountDTOMapper.toAccountDTO(transactionReqDTO.getAccount()),
                    CardDTOMapper.toCardDTO(transactionReqDTO.getCard()),
                    AccountDTOMapper.toAccountDTO(transactionReqDTO.getAccountReceiver()),
                    transactionReqDTO.getReceiverCustomerId());
            case "SP" -> new PaymentStoreDTO(
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    AccountDTOMapper.toAccountDTO(transactionReqDTO.getAccount()),
                    CardDTOMapper.toCardDTO(transactionReqDTO.getCard()),
                    transactionReqDTO.getMarketName());
            case "WP" -> new PaymentWebDTO(
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    AccountDTOMapper.toAccountDTO(transactionReqDTO.getAccount()),
                    CardDTOMapper.toCardDTO(transactionReqDTO.getCard()),
                    transactionReqDTO.getWebsite());
            case "BD" -> new BranchDepositDTO(
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    AccountDTOMapper.toAccountDTO(transactionReqDTO.getAccount()),
                    CardDTOMapper.toCardDTO(transactionReqDTO.getCard()),
                    transactionReqDTO.getBranchName());
            default -> throw new RuntimeException("Invalid transaction type");
        };

        return transaction;

    }

    public static TransactionCommand toTransactionCommand(TransactionReqDTO transactionReqDTO) {
        if (transactionReqDTO == null) return null;
        TransactionCommand transaction = null;
        transaction = switch (transactionReqDTO.getTransactionType()) {
            case "ATM" -> new AtmTransactionCommand(
                    null,
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    CardDTOMapper.toCardRequest(transactionReqDTO.getCard()),
                    transactionReqDTO.getAtmName(),
                    transactionReqDTO.getOperationType()
            );
            case "BA" -> new AccountDepositCommand(
                    null,
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    CardDTOMapper.toCardRequest(transactionReqDTO.getCard()),
                    AccountDTOMapper.toAccountDTO(transactionReqDTO.getAccountReceiver()),
                    transactionReqDTO.getReceiverCustomerId());
            case "SP" -> new PaymentStoreCommand(
                    null,
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    CardDTOMapper.toCardRequest(transactionReqDTO.getCard()),
                    transactionReqDTO.getMarketName());
            case "WP" -> new PaymentWebCommand(
                    null,
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    CardDTOMapper.toCardRequest(transactionReqDTO.getCard()),
                    transactionReqDTO.getWebsite());
            case "BD" -> new BranchDepositCommand(
                    null,
                    null,
                    transactionReqDTO.getCustomerId(),
                    transactionReqDTO.getDescription(),
                    transactionReqDTO.getAmount(),
                    transactionReqDTO.getTransactionType(),
                    transactionReqDTO.getTransactionFee(),
                    null,
                    CardDTOMapper.toCardRequest(transactionReqDTO.getCard()),
                    transactionReqDTO.getBranchName());
            default -> throw new RuntimeException("Invalid transaction type");
        };

        return transaction;

    }
}
