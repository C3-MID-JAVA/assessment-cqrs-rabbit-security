package ec.com.sofka.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.sofka.data.AuthErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class AccessErrorHandler implements ServerAccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        AuthErrorResponse errorResponse =
                new AuthErrorResponse("Access Denied","Only ADMIN role can access this resource");

        return Mono.fromCallable(() -> objectMapper.writeValueAsString(errorResponse))
                .flatMap(json -> exchange.getResponse()
                        .writeWith(Mono.just(exchange.getResponse()
                                .bufferFactory()
                                .wrap(json.getBytes(StandardCharsets.UTF_8)))))
                .onErrorResume(e -> Mono.empty());
    }
}
