package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.UserRepository;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;

public class UserSavedViewUseCase implements IUseCaseAccept<UserDTO> {
    private final UserRepository userRepository;

    public UserSavedViewUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void accept(UserDTO user) {
        userRepository.save(user)
                .subscribe();
    }

}