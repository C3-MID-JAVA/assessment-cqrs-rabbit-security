package ec.com.sofka.globalexceptions;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import ec.com.sofka.ConflictException;
import ec.com.sofka.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalErrorHandler {


    public Mono<ServerResponse> handleException(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof MismatchedInputException) {
            String message = "Invalid or missing fields in JSON request";
            return createErrorResponse(exchange, new IllegalArgumentException(message), HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ConflictException) {
            return createErrorResponse(exchange, ex, HttpStatus.CONFLICT);
        } else if (ex instanceof IllegalArgumentException) {
            return createErrorResponse(exchange, ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof NoSuchElementException) {
            return createErrorResponse(exchange, ex, HttpStatus.NOT_FOUND);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            return createErrorResponse(exchange, ex, HttpStatus.METHOD_NOT_ALLOWED);
        } else if (ex instanceof RuntimeException) {
            return createErrorResponse(exchange, ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return createErrorResponse(exchange, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Mono<ServerResponse> createErrorResponse(ServerWebExchange exchange, Throwable ex, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

}
