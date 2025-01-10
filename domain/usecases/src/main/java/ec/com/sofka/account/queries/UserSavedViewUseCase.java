package ec.com.sofka.account.queries;

import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;

public class UserSavedViewUseCase implements IUseCaseAccept<UserDTO,Void> {

    private final UserRepository userRepository;

    public UserSavedViewUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void accept(UserDTO request) {
        userRepository.save(request).subscribe();
    }

}
