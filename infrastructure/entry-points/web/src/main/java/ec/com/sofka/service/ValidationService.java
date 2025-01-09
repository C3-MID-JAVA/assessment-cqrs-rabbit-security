package ec.com.sofka.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import reactor.core.publisher.Mono;
import org.springframework.validation.Validator;

import java.util.stream.Collectors;
@Service
public class ValidationService {

    private final Validator validator;
    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    /**
     * Valida el objeto proporcionado utilizando el validador y devuelve un Mono con el objeto si es válido.
     * Si no es válido, lanza una excepción con el mensaje de error correspondiente.
     * @param object El objeto que se desea validar
     * @param clazz La clase del objeto (para la validación de anotaciones)
     * @param <T> El tipo del objeto a validar
     * @return Mono del objeto validado
     */
    public <T> Mono<T> validate(T object, Class<?> clazz) {
        var bindingResult = new BeanPropertyBindingResult(object, clazz.getName());
        validator.validate(object, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            // Retorna un Mono de error con el mensaje de validación
            return Mono.error(new IllegalArgumentException(errorMessage));
        }

        return Mono.just(object);
    }
}
