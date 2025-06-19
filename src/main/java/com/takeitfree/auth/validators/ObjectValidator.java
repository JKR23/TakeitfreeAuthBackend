package com.takeitfree.auth.validators;

import com.takeitfree.auth.exceptions.ObjectNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

        if (!constraintViolations.isEmpty()) {
            Set<String> violationMessages = constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());

            throw new ObjectNotValidException(violationMessages);
        }
    }
}
