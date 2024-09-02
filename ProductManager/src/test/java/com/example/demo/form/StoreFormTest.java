package com.example.demo.form;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StoreFormTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidStoreForm() {
        StoreForm form = new StoreForm();
        form.setId(1L);
        form.setName("Store");
        form.setAddress("東京都");

        Set<ConstraintViolation<StoreForm>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidStoreForm_NullId() {
        StoreForm form = new StoreForm();
        form.setName("Store");
        form.setAddress("東京都");

        Set<ConstraintViolation<StoreForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("null は許可されていません", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidStoreForm_BlankName() {
        StoreForm form = new StoreForm();
        form.setId(1L);
        form.setName("");
        form.setAddress("東京都");

        Set<ConstraintViolation<StoreForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals("空白は許可されていません", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidStoreForm_BlankAddress() {
        StoreForm form = new StoreForm();
        form.setId(1L);
        form.setName("Store");
        form.setAddress("");

        Set<ConstraintViolation<StoreForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals("空白は許可されていません", violations.iterator().next().getMessage());
    }
}
