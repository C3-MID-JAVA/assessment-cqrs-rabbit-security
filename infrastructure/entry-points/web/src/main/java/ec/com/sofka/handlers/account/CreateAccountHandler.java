package ec.com.sofka.handlers.account;

import ec.com.sofka.commands.usecases.account.CreateAccountUseCase;
import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.data.AccountResponseDTO;
import ec.com.sofka.mapper.AccountModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CreateAccountHandler {
    private final CreateAccountUseCase createAccountUseCase;

    public CreateAccountHandler(CreateAccountUseCase createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
    }

    public Mono<AccountResponseDTO> save(AccountRequestDTO accountRequestDTO) {
        return createAccountUseCase.execute(AccountModelMapper.mapToRequest(accountRequestDTO))
                .map(AccountModelMapper::mapToDTO);
    }

}