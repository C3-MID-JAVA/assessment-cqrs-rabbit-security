package ec.com.sofka.mapper;

import ec.com.sofka.ROLE;
import ec.com.sofka.dto.AdminRequestDTO;
import ec.com.sofka.dto.LoginRequestDTO;
import ec.com.sofka.usecases.command.LoginAdminCommand;
import ec.com.sofka.usecases.command.RegisterAdminCommand;

public class AdminMapper {
    public static RegisterAdminCommand toRegisterCommand(AdminRequestDTO adminRequestDTO) {
        return new RegisterAdminCommand(
                adminRequestDTO.getEmail(),
                adminRequestDTO.getPassword(),
                ROLE.valueOf(adminRequestDTO.getRole())
        );
    }

    public static LoginAdminCommand toLoginCommand(LoginRequestDTO adminRequestDTO) {
        return new LoginAdminCommand(
                adminRequestDTO.getEmail(),
                adminRequestDTO.getPassword()
        );
    }
}
