package com.example.demo.test;

import com.example.demo.validation.PermissionIdValidator;
import com.example.demo.validation.PositionIdValidator;
import com.example.demo.validation.StoreIdValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;

public class MockConstraintValidatorFactory implements ConstraintValidatorFactory {

    private final PermissionIdValidator permissionIdValidator;
    private final PositionIdValidator positionIdValidator;
    private final StoreIdValidator storeIdValidator;

    public MockConstraintValidatorFactory(PermissionIdValidator permissionIdValidator, PositionIdValidator positionIdValidator, StoreIdValidator storeIdValidator) {
        this.permissionIdValidator = permissionIdValidator;
        this.positionIdValidator = positionIdValidator;
        this.storeIdValidator = storeIdValidator;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        if (key == PermissionIdValidator.class) {
            return (T) permissionIdValidator;
        } else if (key == PositionIdValidator.class) {
            return (T) positionIdValidator;
        } else if (key == StoreIdValidator.class) {
            return (T) storeIdValidator;
        }
        try {
            return key.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
    }
}
