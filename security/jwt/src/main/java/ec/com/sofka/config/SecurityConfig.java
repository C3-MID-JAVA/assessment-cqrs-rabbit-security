package ec.com.sofka.config;


import ec.com.sofka.filters.JWTAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JWTAuthFilter jwtAuthFilter,
            ReactiveAuthenticationManager authManager
    ) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Deshabilitar CSRF
                .authorizeExchange(exchanges -> exchanges
                        // Permitir acceso a Swagger UI, documentación y recursos estáticos
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/api/v1/user/create").permitAll() // Ruta pública de creación de usuario
                        .pathMatchers("/api/v1/user/authenticate").permitAll() // Ruta pública de autenticación
                        .anyExchange().authenticated() // Resto de rutas protegidas
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Seguridad básica
                .authenticationManager(authManager)
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Filtro JWT
                .build();
    }
}