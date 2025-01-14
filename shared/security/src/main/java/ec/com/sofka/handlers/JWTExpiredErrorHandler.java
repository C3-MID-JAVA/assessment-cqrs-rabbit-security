package ec.com.sofka.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.sofka.data.AuthErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class JWTExpiredErrorHandler implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        AuthErrorResponse errorResponse =
                new AuthErrorResponse("Access Denied","The token has expired");

        return Mono.fromCallable(() -> objectMapper.writeValueAsString(errorResponse))
                .flatMap(json -> exchange.getResponse()
                        .writeWith(Mono.just(exchange.getResponse()
                                .bufferFactory()
                                .wrap(json.getBytes(StandardCharsets.UTF_8)))))
                .onErrorResume(e -> Mono.empty());
    }
}
