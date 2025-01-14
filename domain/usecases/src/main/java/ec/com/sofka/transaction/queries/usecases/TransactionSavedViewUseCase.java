package ec.com.sofka.transaction.queries.usecases;

import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;

public class TransactionSavedViewUseCase implements IUseCaseAccept<TransactionDTO, Void> {

    private final TransactionRepository transactionRepository;

    public TransactionSavedViewUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void accept(TransactionDTO request) {
        transactionRepository.save(request).subscribe();
    }
}
