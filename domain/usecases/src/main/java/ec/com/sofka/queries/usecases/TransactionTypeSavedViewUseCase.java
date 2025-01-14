package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;

public class TransactionTypeSavedViewUseCase implements IUseCaseAccept<TransactionTypeDTO> {
    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeSavedViewUseCase(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    public void accept(TransactionTypeDTO transactionType) {
        transactionTypeRepository.save(transactionType)
                .subscribe();
    }

}