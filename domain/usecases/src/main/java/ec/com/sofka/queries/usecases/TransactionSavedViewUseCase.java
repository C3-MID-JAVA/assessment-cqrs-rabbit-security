package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.interfaces.IUseCaseTransactionAccept;

public class TransactionSavedViewUseCase  implements IUseCaseTransactionAccept<TransactionDTO, Void> {
    private final TransactionRepository transactionRepository;

    public TransactionSavedViewUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void accept(TransactionDTO transactionDTO) {
        transactionRepository.save(transactionDTO).subscribe();

    }
}
