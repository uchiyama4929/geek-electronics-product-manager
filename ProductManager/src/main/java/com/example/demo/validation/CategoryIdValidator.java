package com.example.demo.validation;

import com.example.demo.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CategoryIdValidator implements ConstraintValidator<ValidCategoryId, String> {

    private final CategoryRepository categoryRepository;

    public CategoryIdValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void initialize(ValidCategoryId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        try {
            Long categoryId = Long.parseLong(value);
            return categoryRepository.existsById(categoryId);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
