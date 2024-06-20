package com.example.demo.validation;

import com.example.demo.repository.PositionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PositionIdValidator implements ConstraintValidator<ValidPositionId, String> {

    private final PositionRepository positionRepository;

    public PositionIdValidator(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public void initialize(ValidPositionId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        try {
            Long positionId = Long.parseLong(value);
            return positionRepository.existsById(positionId);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
