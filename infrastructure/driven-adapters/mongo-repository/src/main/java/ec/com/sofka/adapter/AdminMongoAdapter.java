package ec.com.sofka.adapter;

import ec.com.sofka.AdminDTO;
import ec.com.sofka.data.AdminEntity;
import ec.com.sofka.database.bank.AdminMongoRepository;
import ec.com.sofka.gateway.AdminRepository;
import ec.com.sofka.mapper.AdminMapperEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class AdminMongoAdapter implements AdminRepository {
    private final AdminMongoRepository adminMongoRepository;

    public AdminMongoAdapter(AdminMongoRepository adminMongoRepository) {
        this.adminMongoRepository = adminMongoRepository;
    }

    @Override
    public Mono<AdminDTO> save(AdminDTO adminDTO) {
        AdminEntity adminEntity = AdminMapperEntity.toEntity(adminDTO);
        return adminMongoRepository.save(adminEntity).map(AdminMapperEntity::fromEntity);
    }

    @Override
    public Mono<AdminDTO> findByEmail(String email) {
        return adminMongoRepository.findByEmail(email).map(AdminMapperEntity::fromEntity);
    }

}
