package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.appservice.gateway.ITransactionRepository;
import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.interfaces.IUseCaseSave;
import org.springframework.stereotype.Service;

@Service
public class TransactionSavedViewUseCase implements IUseCaseSave<TransactionDTO> {
    private final ITransactionRepository transactionRepository;

    public TransactionSavedViewUseCase(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(TransactionDTO save) {
        transactionRepository.save(save);
    }
}
