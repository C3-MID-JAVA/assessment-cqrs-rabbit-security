package ec.com.sofka;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthFilter implements WebFilter {
    private final JwtServiceAdapter jwtServiceAdapter;

    public JwtAuthFilter(JwtServiceAdapter jwtServiceAdapter) {
        this.jwtServiceAdapter = jwtServiceAdapter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractTokenFromRequest(exchange.getRequest());

        /*System.out.println(token);*/
        if (token != null && jwtServiceAdapter.isTokenValid(token)) {
            String username = jwtServiceAdapter.extractUsername(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.emptyList()
            );

            SecurityContext securityContext = new SecurityContextImpl(authentication);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
        }

        return chain.filter(exchange);
    }

    private String extractTokenFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
