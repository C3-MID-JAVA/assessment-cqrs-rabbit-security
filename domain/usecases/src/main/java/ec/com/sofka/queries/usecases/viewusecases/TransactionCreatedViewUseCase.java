package ec.com.sofka.queries.usecases.viewusecases;

import ec.com.sofka.gateway.ITransactionRepository;
import ec.com.sofka.gateway.dto.transaction.TransactionDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;

public class TransactionCreatedViewUseCase implements IUseCaseAccept<TransactionDTO, Void> {
    private final ITransactionRepository transactionRepository;


    public TransactionCreatedViewUseCase(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Override
    public void accept(TransactionDTO dto) {
        transactionRepository.save(dto).subscribe();
    }
}
