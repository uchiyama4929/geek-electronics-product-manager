package com.example.demo.form;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginFormTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidLoginForm() {
        LoginForm form = new LoginForm();
        form.setEmail("test@example.com");
        form.setPassword("password");

        Set<ConstraintViolation<LoginForm>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidLoginForm_BlankEmail() {
        LoginForm form = new LoginForm();
        form.setEmail("");
        form.setPassword("password");

        Set<ConstraintViolation<LoginForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("空白は許可されていません")));
    }

    @Test
    public void testInvalidLoginForm_InvalidEmail() {
        LoginForm form = new LoginForm();
        form.setEmail("invalid-email");
        form.setPassword("password");

        Set<ConstraintViolation<LoginForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("電子メールアドレスとして正しい形式にしてください")));
    }

    @Test
    public void testInvalidLoginForm_BlankPassword() {
        LoginForm form = new LoginForm();
        form.setEmail("test@example.com");
        form.setPassword("");

        Set<ConstraintViolation<LoginForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("空白は許可されていません")));
    }
}
