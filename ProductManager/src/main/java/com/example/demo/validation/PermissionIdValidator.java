package com.example.demo.validation;

import com.example.demo.repository.PermissionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PermissionIdValidator implements ConstraintValidator<ValidPermissionId, String> {

    private final PermissionRepository permissionRepository;

    public PermissionIdValidator(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void initialize(ValidPermissionId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        try {
            Long permissionId = Long.parseLong(value);
            return permissionRepository.existsById(permissionId);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
