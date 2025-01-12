package ec.com.sofka.generics.interfaces;


public interface IUseCaseTransactionAccept <TransactionDTO, Void>{
    void accept(TransactionDTO transactionDTO);
}
