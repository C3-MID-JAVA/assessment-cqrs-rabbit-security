package ec.com.sofka.exceptions;


import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.stream.Collectors;

@Component
public class BodyRequestValidator {

    private final Validator validator;

    public BodyRequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T target) {
        Errors errors = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
        validator.validate(target, errors);

        if (errors.hasErrors()) {
            String errorMessages = errors.getFieldErrors().stream()
                    .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.joining(" OR "));
            throw new ValidationException(errorMessages);
        }
    }
}
