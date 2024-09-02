package com.example.demo.form;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderFormTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidOrderForm() {
        OrderForm form = new OrderForm();
        form.setProductId(1L);
        form.setQuantity("10");

        Set<ConstraintViolation<OrderForm>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidOrderForm_NullProductId() {
        OrderForm form = new OrderForm();
        form.setQuantity("10");

        Set<ConstraintViolation<OrderForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("null は許可されていません", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidOrderForm_BlankQuantity() {
        OrderForm form = new OrderForm();
        form.setProductId(1L);
        form.setQuantity("");

        Set<ConstraintViolation<OrderForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("空白は許可されていません", violations.iterator().next().getMessage());
    }
}
