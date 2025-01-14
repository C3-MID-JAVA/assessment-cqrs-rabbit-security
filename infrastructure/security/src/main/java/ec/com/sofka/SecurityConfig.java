package ec.com.sofka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, e) -> {
                            throw new AuthenticationException("Unauthorized access") {};
                        })
                        .accessDeniedHandler((exchange, e) -> {
                            throw new AccessDeniedException("Access is forbidden") {};
                        })
                )
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers( "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/admin/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/users/**", "/transactions/**", "/accounts/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/users/**", "/transactions/**", "/accounts/**")
                        .hasRole(RoleEnum.SUPER_ADMIN.name())
                        .anyExchange().authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

