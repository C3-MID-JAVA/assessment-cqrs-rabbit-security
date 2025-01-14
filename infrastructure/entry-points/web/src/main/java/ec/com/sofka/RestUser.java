package ec.com.sofka;

import ec.com.sofka.data.user.UserRequestDTO;
import ec.com.sofka.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
public class RestUser {
    private final UserHandler userHandler;

    public RestUser(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/user/register"), this::register)
                .andRoute(RequestPredicates.POST("/user/login"), this::login);
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .flatMap(userHandler::create)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue("Error registering user: " + e.getMessage()));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .flatMap(userHandler::login)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .bodyValue("Error logging in: " + e.getMessage()));
    }
}