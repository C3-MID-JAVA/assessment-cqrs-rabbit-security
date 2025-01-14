package ec.com.sofka.config;

import ec.com.sofka.filters.JWTAuthFilter;
import ec.com.sofka.handlers.AccessErrorHandler;
import ec.com.sofka.handlers.JWTExpiredErrorHandler;
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
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/api/v1/user/**").permitAll()
                        .pathMatchers("/api/v1/account").hasRole("ADMIN")
                        .pathMatchers("/api/v1/card/byAccount").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new AccessErrorHandler())
                        .authenticationEntryPoint(new JWTExpiredErrorHandler())
                )
                .authenticationManager(authManager)
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}
