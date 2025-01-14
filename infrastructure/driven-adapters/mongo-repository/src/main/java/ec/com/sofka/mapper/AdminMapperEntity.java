package ec.com.sofka.mapper;

import ec.com.sofka.AdminDTO;
import ec.com.sofka.data.AdminEntity;

public class AdminMapperEntity {
    public static AdminEntity toEntity(AdminDTO adminDTO){
        return new AdminEntity(adminDTO.getEmail(), adminDTO.getPassword(), adminDTO.getRole());
    }

    public static AdminDTO fromEntity(AdminEntity adminEntity){
        return new AdminDTO(adminEntity.getId(), adminEntity.getEmail(), adminEntity.getPassword(), adminEntity.getRole());
    }
}
