package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.ErrorLogRepository;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;
import ec.com.sofka.model.ErrorMessage;

public class ErrorLogSavedViewUseCase implements IUseCaseAccept<ErrorMessage> {
    private final ErrorLogRepository errorLogRepository;

    public ErrorLogSavedViewUseCase(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    @Override
    public void accept(ErrorMessage errorMessage) {
        errorLogRepository.save(errorMessage)
                .subscribe();
    }
}
