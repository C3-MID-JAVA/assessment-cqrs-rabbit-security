package ec.com.sofka.gateway;

import ec.com.sofka.AdminDTO;
import reactor.core.publisher.Mono;

public interface AdminRepository {
    Mono<AdminDTO> save(AdminDTO adminDTO);
    Mono<AdminDTO> findByEmail(String email);
}