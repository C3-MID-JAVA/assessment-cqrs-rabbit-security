package ec.com.sofka.data;

import ec.com.sofka.ConstansTrType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Request representing a transaction with all its details")
public class TransactionReqDTO {

    private String customerId;

    @NotNull(message = "description" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "description" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Description of the transaction", example = "ATM Withdrawal")
    private String description;

    @NotNull(message = "amount" + ConstansTrType.NOT_NULL_FIELD)
    @PositiveOrZero(message = "amount must be positive or zero")
    @Schema(description = "Amount for the transaction", example = "100.00")
    private BigDecimal amount;

    @NotNull(message = "transactionType" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "transactionType" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Type of the transaction (e.g., BD, ATM)", example = "ATM")
    private String transactionType;

    @NotNull(message = "transactionFee" + ConstansTrType.NOT_NULL_FIELD)
    @PositiveOrZero(message = "transactionFee must be positive or zero")
    @Schema(description = "Fee associated with the transaction", example = "0")
    private BigDecimal transactionFee;

    @NotNull(message = "account" + ConstansTrType.NOT_NULL_FIELD)
    @Schema(hidden = true)
    private AccountReqDTO account;

    @NotNull(message = "card" + ConstansTrType.NOT_NULL_FIELD)
    @Schema(hidden = true)
    private CardReqDTO card;


    @Schema(hidden = true)
    private String website;
    @Schema(hidden = true)
    private String marketName;
    @Schema(hidden = true)
    private String atmName;
    @Schema(hidden = true)
    private String operationType;
    @Schema(hidden = true)
    private String branchName;
    @Schema(hidden = true)
    private AccountReqDTO accountReceiver;
    private String receiverCustomerId;

    public TransactionReqDTO(String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, AccountReqDTO account, CardReqDTO card, String receiverCustomerId) {
        this.customerId = customerId;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionFee = transactionFee;
        this.account = account;
        this.card = card;
        this.receiverCustomerId = receiverCustomerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
    }

    public AccountReqDTO getAccount() {
        return account;
    }

    public void setAccount(AccountReqDTO account) {
        this.account = account;
    }

    public CardReqDTO getCard() {
        return card;
    }

    public void setCard(CardReqDTO card) {
        this.card = card;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getAtmName() {
        return atmName;
    }

    public void setAtmName(String atmName) {
        this.atmName = atmName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public AccountReqDTO getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(AccountReqDTO accountReceiver) {
        this.accountReceiver = accountReceiver;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getReceiverCustomerId() {
        return receiverCustomerId;
    }

    public void setReceiverCustomerId(String receiverCustomerId) {
        this.receiverCustomerId = receiverCustomerId;
    }
}

