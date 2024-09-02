package com.example.demo.form;

import com.example.demo.test.MockConstraintValidatorFactory;
import com.example.demo.validation.PermissionIdValidator;
import com.example.demo.validation.PositionIdValidator;
import com.example.demo.validation.StoreIdValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagerFormTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        PermissionIdValidator mockPermissionIdValidator = Mockito.mock(PermissionIdValidator.class);
        PositionIdValidator mockPositionIdValidator = Mockito.mock(PositionIdValidator.class);
        StoreIdValidator mockStoreIdValidator = Mockito.mock(StoreIdValidator.class);

        Mockito.when(mockPermissionIdValidator.isValid(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Mockito.when(mockPositionIdValidator.isValid(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Mockito.when(mockStoreIdValidator.isValid(Mockito.anyString(), Mockito.any())).thenReturn(true);

        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .constraintValidatorFactory(new MockConstraintValidatorFactory(mockPermissionIdValidator, mockPositionIdValidator, mockStoreIdValidator))
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidManagerForm() {
        ManagerForm form = new ManagerForm();
        form.setStoreId("1");
        form.setPositionId("1");
        form.setPermissionId("1");
        form.setLastName("Test");
        form.setFirstName("User");
        form.setEmail("test@example.com");
        form.setPhoneNumber("0123456789");
        form.setPassword("password");
        form.setPasswordConfirmation("password");

        Set<ConstraintViolation<ManagerForm>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidManagerForm_BlankFields() {
        ManagerForm form = new ManagerForm();
        form.setStoreId("");
        form.setPositionId("");
        form.setPermissionId("");
        form.setLastName("");
        form.setFirstName("");
        form.setEmail("");
        form.setPhoneNumber("");
        form.setPassword("");
        form.setPasswordConfirmation("");

        Set<ConstraintViolation<ManagerForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals(9, violations.size());
    }

    @Test
    public void testInvalidManagerForm_InvalidEmail() {
        ManagerForm form = new ManagerForm();
        form.setStoreId("1");
        form.setPositionId("1");
        form.setPermissionId("1");
        form.setLastName("Test");
        form.setFirstName("User");
        form.setEmail("invalid-email");
        form.setPhoneNumber("0123456789");
        form.setPassword("password");
        form.setPasswordConfirmation("password");

        Set<ConstraintViolation<ManagerForm>> violations = validator.validate(form);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("電子メールアドレスとして正しい形式にしてください", violations.iterator().next().getMessage());

    }

    @Test
    public void testInvalidManagerForm_PasswordMismatch() {
        ManagerForm form = new ManagerForm();
        form.setStoreId("1");
        form.setPositionId("1");
        form.setPermissionId("1");
        form.setLastName("Test");
        form.setFirstName("User");
        form.setEmail("test@example.com");
        form.setPhoneNumber("0123456789");
        form.setPassword("password");
        form.setPasswordConfirmation("differentPassword");

        Set<ConstraintViolation<ManagerForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size(), "There should be 1 validation error");
        assertEquals("パスワードが確認用と一致しません。", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidManagerForm_PhoneNumberTooShort() {
        ManagerForm form = new ManagerForm();
        form.setStoreId("1");
        form.setPositionId("1");
        form.setPermissionId("1");
        form.setLastName("Test");
        form.setFirstName("User");
        form.setEmail("test@example.com");
        form.setPhoneNumber("012345678");
        form.setPassword("password");
        form.setPasswordConfirmation("password");

        Set<ConstraintViolation<ManagerForm>> violations = validator.validate(form);
        assertEquals(1, violations.size());
        assertEquals("10 から 11 の間のサイズにしてください", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidManagerForm_PhoneNumberTooLong() {
        ManagerForm form = new ManagerForm();
        form.setStoreId("1");
        form.setPositionId("1");
        form.setPermissionId("1");
        form.setLastName("Test");
        form.setFirstName("User");
        form.setEmail("test@example.com");
        form.setPhoneNumber("0801234567890");  // too long
        form.setPassword("password");
        form.setPasswordConfirmation("password");

        Set<ConstraintViolation<ManagerForm>> violations = validator.validate(form);
        assertEquals(1, violations.size());
        assertEquals("10 から 11 の間のサイズにしてください", violations.iterator().next().getMessage());
    }
}
