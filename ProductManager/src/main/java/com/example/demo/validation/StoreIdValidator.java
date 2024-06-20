package com.example.demo.validation;


import com.example.demo.repository.StoreRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class StoreIdValidator implements ConstraintValidator<ValidStoreId, String> {

    private final StoreRepository storeRepository;

    public StoreIdValidator(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void initialize(ValidStoreId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        try {
            return storeRepository.existsById(Long.valueOf(value));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
