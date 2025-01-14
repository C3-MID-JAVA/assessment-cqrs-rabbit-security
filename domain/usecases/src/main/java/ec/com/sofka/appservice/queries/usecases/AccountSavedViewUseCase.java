package ec.com.sofka.appservice.queries.usecases;

import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.generics.interfaces.IUseCaseSave;
import org.springframework.stereotype.Service;

@Service
public class AccountSavedViewUseCase implements IUseCaseSave<AccountDTO> {

    private final IAccountRepository accountRepository;

    public AccountSavedViewUseCase(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void save(AccountDTO accountDTO) {
        accountRepository.save(accountDTO).subscribe();
    }
}
