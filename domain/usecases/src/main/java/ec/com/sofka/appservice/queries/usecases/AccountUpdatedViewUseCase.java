package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.generics.interfaces.IUseCaseSave;
import ec.com.sofka.generics.interfaces.IUseCaseUpdate;
import org.springframework.stereotype.Service;

@Service
public class AccountUpdatedViewUseCase implements IUseCaseUpdate<AccountDTO> {

    private final IAccountRepository accountRepository;

    public AccountUpdatedViewUseCase(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void update(AccountDTO save) {
        accountRepository.update(save);
    }
}
