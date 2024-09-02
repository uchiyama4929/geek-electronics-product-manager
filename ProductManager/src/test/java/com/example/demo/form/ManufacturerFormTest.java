package com.example.demo.form;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ManufacturerFormTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidManufacturerForm() {
        ManufacturerForm form = new ManufacturerForm();
        form.setId("1");
        form.setName("Test Manufacturer");

        Set<ConstraintViolation<ManufacturerForm>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidManufacturerForm_BlankName() {
        ManufacturerForm form = new ManufacturerForm();
        form.setId("1");
        form.setName("");

        Set<ConstraintViolation<ManufacturerForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("空白は許可されていません", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidManufacturerForm_NullName() {
        ManufacturerForm form = new ManufacturerForm();
        form.setId("1");
        form.setName(null);

        Set<ConstraintViolation<ManufacturerForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("空白は許可されていません", violations.iterator().next().getMessage());
    }
}
