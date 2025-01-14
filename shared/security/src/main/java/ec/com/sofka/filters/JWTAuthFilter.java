package ec.com.sofka.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.sofka.data.AuthErrorResponse;
import ec.com.sofka.exceptions.GlobalExceptionsHandler;
import ec.com.sofka.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JWTAuthFilter implements WebFilter {
    private final JWTService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain filterChain) {

        final String authHeader =
                exchange
                        .getRequest()
                        .getHeaders()
                        .getFirst("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return filterChain.filter(exchange);
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);

            if (username != null) {
                var authoritiesClaims = jwtService.extractAllClaims(jwt).get("roles");
                var authorities =
                        authoritiesClaims != null ?
                                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+ authoritiesClaims) :
                                AuthorityUtils.NO_AUTHORITIES;
                System.out.println("authoritiesClaims "+authoritiesClaims);

                UserDetails userDetails =
                        User
                                .withUsername(username)
                                .password("")
                                .authorities(authorities)
                                .build();

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    authorities);
                    return filterChain
                            .filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
                }
            }
            return filterChain.filter(exchange);
        } catch (ExpiredJwtException ex) {
            return handleException(exchange, "The token has expired", "ACCESS_DENIED");
        } catch (Exception ex) {
            return handleException(exchange, "Authentication error", "ACCESS_DENIED");
        }
    }

    private Mono<Void> handleException(ServerWebExchange exchange,
                                       String errorMessage,
                                       String error) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        AuthErrorResponse errorResponse = new AuthErrorResponse(error, errorMessage);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(bytes);

            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error writing response", e));
        }
    }
}
