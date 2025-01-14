package ec.com.sofka;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.AccountNumber;
import ec.com.sofka.account.values.objects.AccountType;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.OwnerName;
import ec.com.sofka.aggregate.account.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.account.events.AccountCreated;
import ec.com.sofka.aggregate.account.events.CardCreated;
import ec.com.sofka.aggregate.transaction.events.TransactionCreated;
import ec.com.sofka.card.Card;
import ec.com.sofka.card.values.CardId;
import ec.com.sofka.card.values.objects.*;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.gateway.dto.transaction.*;
import ec.com.sofka.queries.usecases.viewusecases.AccountSavedViewUseCase;
import ec.com.sofka.queries.usecases.viewusecases.CardSavedViewUseCase;
import ec.com.sofka.queries.usecases.viewusecases.TransactionCreatedViewUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusListenerTest {

    @Mock
    private AccountSavedViewUseCase accountSavedViewUseCase;
    @Mock
    private CardSavedViewUseCase cardSavedViewUseCase;
    @Mock
    private TransactionCreatedViewUseCase transactionCreatedViewUseCase;
    @InjectMocks
    private BusListener busListener;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Should invoke AccountSavedUseCase when AccountCreated event is received")
    void shouldInvokeAccountSavedUseCaseWhenAccountCreatedEventReceived() {

        AccountCreated event = new AccountCreated("1",
                "123456789",
                BigDecimal.valueOf(1000.0),
                "John Doe",
                "SAVINGS");

        busListener.receiveAccountCreated(event);

        ArgumentCaptor<AccountDTO> captor = ArgumentCaptor.forClass(AccountDTO.class);
        verify(accountSavedViewUseCase, times(1)).accept(captor.capture());
        AccountDTO capturedDTO = captor.getValue();

        assertThat(capturedDTO).isNotNull();
        assertThat(capturedDTO.getId()).isEqualTo("1");
        assertThat(capturedDTO.getBalance()).isEqualTo(BigDecimal.valueOf(1000.0));
        assertThat(capturedDTO.getOwnerName()).isEqualTo("John Doe");
        assertThat(capturedDTO.getAccountNumber()).isEqualTo("123456789");
        assertThat(capturedDTO.getAccountType()).isEqualTo("SAVINGS");
    }

    @Test
    @DisplayName("Should invoke CardSavedUseCase when CardCreated event is received")
    void shouldInvokeCardSavedUseCaseWhenCardCreatedEventReceived() {

        CardCreated event = new CardCreated(
                "1", "Gold Card", "1234-5678-9012-3456", "CREDIT", "ACTIVE",
                "12/30", "123", BigDecimal.valueOf(5000.0), "John Doe",
                new Account(AccountId.of("1"
                ), Balance.of(BigDecimal.valueOf(1000.0)), AccountNumber.of("123456789"), OwnerName.of("John Doe"), AccountType.of("SAVINGS"))
        );


        busListener.receiveCardCreated(event);


        ArgumentCaptor<CardDTO> captor = ArgumentCaptor.forClass(CardDTO.class);
        verify(cardSavedViewUseCase, times(1)).accept(captor.capture());
        CardDTO capturedDTO = captor.getValue();

        assertThat(capturedDTO).isNotNull();
        assertThat(capturedDTO.getCardName()).isEqualTo("Gold Card");
        assertThat(capturedDTO.getCardNumber()).isEqualTo("1234-5678-9012-3456");
        assertThat(capturedDTO.getCardType()).isEqualTo("CREDIT");
    }

    @Test
    @DisplayName("Should invoke TransactionCreatedUseCase when TransactionCreated event is received")
    void shouldInvokeTransactionCreatedUseCaseWhenTransactionCreatedEventReceived() {

        TransactionCreated event = new TransactionCreated(
                "1", "ATM Withdrawal", BigDecimal.valueOf(500.0), "ATM", BigDecimal.valueOf(5.0), null,
                new Account(AccountId.of("1"),
                        Balance.of(BigDecimal.valueOf(1000.0)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("SAVINGS")),
                new Card(
                        CardId.of("1"),
                        CardName.of("Gold Card"),
                        CardNumber.of("1234567891234567"),
                        CardType.of("CREDIT"),
                        CardStatus.of("ACTIVE"),
                        CardExpiryDate.of("12/30"),
                        CardCVV.of("1234"),
                        CardLimit.of(BigDecimal.valueOf(5000.0)),
                        CardHolderName.of("John Doe"),
                        AccountValue.of(
                                new Account(AccountId.of("1"),
                                        Balance.of(BigDecimal.valueOf(1000.0)),
                                        AccountNumber.of("123456789"),
                                        OwnerName.of("John Doe"),
                                        AccountType.of("SAVINGS"))
                        )
                ),"Main ATM", "WITHDRAWAL",
                null,
                null,
                null,
                null
        );


        busListener.receiveTransactionCreated(event);


        ArgumentCaptor<TransactionDTO> captor = ArgumentCaptor.forClass(TransactionDTO.class);
        verify(transactionCreatedViewUseCase, times(1)).accept(captor.capture());
        TransactionDTO capturedDTO = captor.getValue();

        assertThat(capturedDTO).isNotNull();
        assertThat(capturedDTO.getDescription()).isEqualTo("ATM Withdrawal");
        assertThat(capturedDTO.getAmount()).isEqualTo(BigDecimal.valueOf(500.0));
    }
    @Test
    @DisplayName("Should construct AccountDepositDTO when transaction type is BETWEEN_ACCOUNT")
    void shouldConstructAccountDepositDTOWhenTransactionTypeIsBetweenAccount() {
        TransactionCreated event = new TransactionCreated(
                "1", "Transfer Between Accounts", BigDecimal.valueOf(1000.0), ConstansTrType.BETWEEN_ACCOUNT, BigDecimal.valueOf(0.0), null,
                new Account(AccountId.of("1"),
                        Balance.of(BigDecimal.valueOf(1000.0)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("SAVINGS")),
                null, null, null,
                new Account(AccountId.of("2"),
                        Balance.of(BigDecimal.valueOf(2000.0)),
                        AccountNumber.of("987654321"),
                        OwnerName.of("Jane Doe"),
                        AccountType.of("CHECKING")),
                null, null,null);

        TransactionDTO dto = busListener.constructDTO(event);

        assertThat(dto).isInstanceOf(AccountDepositDTO.class);
        AccountDepositDTO depositDto = (AccountDepositDTO) dto;
        assertThat(depositDto.getDescription()).isEqualTo("Transfer Between Accounts");
        assertThat(depositDto.getAmount()).isEqualTo(BigDecimal.valueOf(1000.0));
        assertThat(depositDto.getAccountReceiver().getAccountNumber()).isEqualTo("987654321");
    }
    @Test
    @DisplayName("Should construct PaymentStoreDTO when transaction type is STORE_PURCHASE")
    void shouldConstructPaymentStoreDTOWhenTransactionTypeIsStorePurchase() {
        TransactionCreated event = new TransactionCreated(
                "1", "Grocery Store Purchase", BigDecimal.valueOf(50.0), ConstansTrType.STORE_PURCHASE, BigDecimal.valueOf(1.0), null,
                new Account(AccountId.of("1"),
                        Balance.of(BigDecimal.valueOf(1000.0)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("SAVINGS")),
                new Card(
                        CardId.of("1"),
                        CardName.of("Gold Card"),
                        CardNumber.of("1234567891234567"),
                        CardType.of("CREDIT"),
                        CardStatus.of("ACTIVE"),
                        CardExpiryDate.of("12/30"),
                        CardCVV.of("1234"),
                        CardLimit.of(BigDecimal.valueOf(5000.0)),
                        CardHolderName.of("John Doe"),
                        AccountValue.of(
                                new Account(AccountId.of("1"),
                                        Balance.of(BigDecimal.valueOf(1000.0)),
                                        AccountNumber.of("123456789"),
                                        OwnerName.of("John Doe"),
                                        AccountType.of("SAVINGS"))
                        )
                ), null, null, null, "SuperMart", null, null);

        TransactionDTO dto = busListener.constructDTO(event);

        assertThat(dto).isInstanceOf(PaymentStoreDTO.class);
        PaymentStoreDTO storeDto = (PaymentStoreDTO) dto;
        assertThat(storeDto.getDescription()).isEqualTo("Grocery Store Purchase");
        assertThat(storeDto.getMarketName()).isEqualTo("SuperMart");
    }

    @Test
    @DisplayName("Should construct PaymentWebDTO when transaction type is WEB_PURCHASE")
    void shouldConstructPaymentWebDTOWhenTransactionTypeIsWebPurchase() {
        TransactionCreated event = new TransactionCreated(
                "1", "Online Purchase", BigDecimal.valueOf(200.0), ConstansTrType.WEB_PURCHASE, BigDecimal.valueOf(2.0), null,
                new Account(AccountId.of("1"),
                        Balance.of(BigDecimal.valueOf(1000.0)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("SAVINGS")),
                new Card(
                        CardId.of("1"),
                        CardName.of("Gold Card"),
                        CardNumber.of("1234567891234567"),
                        CardType.of("CREDIT"),
                        CardStatus.of("ACTIVE"),
                        CardExpiryDate.of("12/30"),
                        CardCVV.of("1234"),
                        CardLimit.of(BigDecimal.valueOf(5000.0)),
                        CardHolderName.of("John Doe"),
                        AccountValue.of(
                                new Account(AccountId.of("1"),
                                        Balance.of(BigDecimal.valueOf(1000.0)),
                                        AccountNumber.of("123456789"),
                                        OwnerName.of("John Doe"),
                                        AccountType.of("SAVINGS"))
                        )
                ), null, null, null, null, "example.com", null);

        TransactionDTO dto = busListener.constructDTO(event);

        assertThat(dto).isInstanceOf(PaymentWebDTO.class);
        PaymentWebDTO webDto = (PaymentWebDTO) dto;
        assertThat(webDto.getDescription()).isEqualTo("Online Purchase");
        assertThat(webDto.getWebsite()).isEqualTo("example.com");
    }
    @Test
    @DisplayName("Should construct PaymentWebDTO when transaction type is BRANCH_DEPOSIT")
    void shouldConstructPaymentWebDTOWhenTransactionTypeIsBranchDeposit() {
        TransactionCreated event = new TransactionCreated(
                "1", "Online Purchase", BigDecimal.valueOf(200.0), ConstansTrType.BRANCH_DEPOSIT, BigDecimal.valueOf(2.0), null,
                new Account(AccountId.of("1"),
                        Balance.of(BigDecimal.valueOf(1000.0)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("SAVINGS")),
                new Card(
                        CardId.of("1"),
                        CardName.of("Gold Card"),
                        CardNumber.of("1234567891234567"),
                        CardType.of("CREDIT"),
                        CardStatus.of("ACTIVE"),
                        CardExpiryDate.of("12/30"),
                        CardCVV.of("1234"),
                        CardLimit.of(BigDecimal.valueOf(5000.0)),
                        CardHolderName.of("John Doe"),
                        AccountValue.of(
                                new Account(AccountId.of("1"),
                                        Balance.of(BigDecimal.valueOf(1000.0)),
                                        AccountNumber.of("123456789"),
                                        OwnerName.of("John Doe"),
                                        AccountType.of("SAVINGS"))
                        )
                ), null, null, null, null, null, "Branch Test");

        TransactionDTO dto = busListener.constructDTO(event);

        assertThat(dto).isInstanceOf(BranchDepositDTO.class);
        BranchDepositDTO branchDto = (BranchDepositDTO) dto;
        assertThat(branchDto.getDescription()).isEqualTo("Online Purchase");
        assertThat(branchDto.getBranchName()).isEqualTo("Branch Test");
    }

    @Test
    @DisplayName("Should process AccountUpdated event and call use case")
    void shouldProcessAccountUpdatedEventAndCallUseCase() {

        AccountBalanceUpdated event = new AccountBalanceUpdated(
                "1",
                "123456789",
                BigDecimal.valueOf(1200.0),
                "John Doe",
                "SAVINGS"
        );


        busListener.receiveAccountUpdated(event);


        ArgumentCaptor<AccountDTO> captor = ArgumentCaptor.forClass(AccountDTO.class);
        verify(accountSavedViewUseCase).accept(captor.capture());

        AccountDTO accountDTO = captor.getValue();


        assertThat(accountDTO.getId()).isEqualTo("1");
        assertThat(accountDTO.getBalance()).isEqualTo(BigDecimal.valueOf(1200.0));
        assertThat(accountDTO.getOwnerName()).isEqualTo("John Doe");
        assertThat(accountDTO.getAccountNumber()).isEqualTo("123456789");
        assertThat(accountDTO.getAccountType()).isEqualTo("SAVINGS");
    }

    @Test
    @DisplayName("Should throw error when constructing AccountDepositDTO for unsupported transaction type")
    void shouldThrowErrorConstructAccountDepositDTOWhenTransactionTypeIsBetweenAccount() {
        TransactionCreated event = new TransactionCreated(
                "1", "Transfer Between Accounts", BigDecimal.valueOf(1000.0), "XD", BigDecimal.valueOf(0.0), null,
                new Account(AccountId.of("1"),
                        Balance.of(BigDecimal.valueOf(1000.0)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("SAVINGS")),
                null, null, null,
                new Account(AccountId.of("2"),
                        Balance.of(BigDecimal.valueOf(2000.0)),
                        AccountNumber.of("987654321"),
                        OwnerName.of("Jane Doe"),
                        AccountType.of("CHECKING")),
                null, null,null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            busListener.constructDTO(event);
        });

        assertThat(exception.getMessage()).isEqualTo("Unsupported transaction type in Mapper");
    }

}