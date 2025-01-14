package ec.com.sofka.handler;

import ec.com.sofka.JwtService;
import ec.com.sofka.data.AuthRequestDTO;
import ec.com.sofka.data.AuthResponseDTO;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthHandler {
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authManager;

    public AuthHandler(JwtService jwtService, ReactiveAuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public Mono<AuthResponseDTO> authenticate(AuthRequestDTO request) {
        try {
            return authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()))
                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                    .map(auth -> {
                        UserDetails userDetails = (UserDetails) auth.getPrincipal();
                        return getAuthResponse(userDetails);
                    });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private AuthResponseDTO getAuthResponse(UserDetails userDetails) {
        var extraClaims = extractAuthorities("roles", userDetails);

        var jwtToken = jwtService.generateToken(userDetails, extraClaims);
        return new AuthResponseDTO(jwtToken);
    }

    private Map<String, Object> extractAuthorities(String key, UserDetails userDetails) {
        Map<String, Object> authorities = new HashMap<>();

        authorities.put(key,
                userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")));

        return authorities;
    }
}
