package ec.com.sofka.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Mono<ResponseEntity<ErrorResponse>> createErrorResponse(Exception ex, HttpStatus status, String path) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                path
        );
        return Mono.just(new ResponseEntity<>(errorResponse, status));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex, ServerWebExchange exchange) {
        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return createErrorResponse(ex, HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().toString());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNoSuchElementException(NoSuchElementException ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND, exchange.getRequest().getPath().toString());
    }

    @ExceptionHandler(ConflictException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleConflictException(ConflictException ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, HttpStatus.CONFLICT, exchange.getRequest().getPath().toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgumentException(IllegalArgumentException ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().toString());
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRuntimeException(RuntimeException ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, exchange.getRequest().getPath().toString());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, exchange.getRequest().getPath().toString());
    }

    @ExceptionHandler(CuentaNoEncontradaException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCuentaNoEncontrada(CuentaNoEncontradaException ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND, exchange.getRequest().getPath().toString());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public Mono<ResponseEntity<String>> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }
}