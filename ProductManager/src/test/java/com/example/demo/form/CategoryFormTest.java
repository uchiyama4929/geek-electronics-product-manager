package com.example.demo.form;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.validation.CategoryIdValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CategoryFormTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.existsById(2L)).thenReturn(true);
        when(categoryRepository.existsById(3L)).thenReturn(true);

        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(categoryRepository))
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCategoryForm() {
        CategoryForm form = new CategoryForm();
        form.setKeyword("Test Keyword");
        form.setLargeCategory("1");
        form.setMiddleCategory("2");
        form.setSmallCategory("3");

        Set<ConstraintViolation<CategoryForm>> violations = validator.validate(form);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidCategoryForm_LongKeyword() {
        CategoryForm form = new CategoryForm();
        form.setKeyword("a".repeat(256));
        form.setLargeCategory("1");
        form.setMiddleCategory("2");
        form.setSmallCategory("3");

        Set<ConstraintViolation<CategoryForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("カテゴリ名は255文字以下で入力してください。")));
    }

    @Test
    public void testInvalidCategoryForm_NonNumericLargeCategory() {
        CategoryForm form = new CategoryForm();
        form.setKeyword("Test Keyword");
        form.setLargeCategory("abc");
        form.setMiddleCategory("2");
        form.setSmallCategory("3");

        Set<ConstraintViolation<CategoryForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("大カテゴリは数字である必要があります")));
    }

    @Test
    public void testInvalidCategoryForm_NonNumericMiddleCategory() {
        CategoryForm form = new CategoryForm();
        form.setKeyword("Test Keyword");
        form.setLargeCategory("1");
        form.setMiddleCategory("abc");
        form.setSmallCategory("3");

        Set<ConstraintViolation<CategoryForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("中カテゴリは数字である必要があります")));
    }

    @Test
    public void testInvalidCategoryForm_NonNumericSmallCategory() {
        CategoryForm form = new CategoryForm();
        form.setKeyword("Test Keyword");
        form.setLargeCategory("1");
        form.setMiddleCategory("2");
        form.setSmallCategory("abc");

        Set<ConstraintViolation<CategoryForm>> violations = validator.validate(form);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("小カテゴリは数字である必要があります")));
    }

    @Configuration
    static class Config {
        @Bean
        public CategoryRepository categoryRepository() {
            return mock(CategoryRepository.class);
        }

        @Bean
        public CategoryIdValidator categoryIdValidator(CategoryRepository categoryRepository) {
            return new CategoryIdValidator(categoryRepository);
        }
    }

    static class SpringConstraintValidatorFactory implements jakarta.validation.ConstraintValidatorFactory {
        private final CategoryRepository categoryRepository;

        public SpringConstraintValidatorFactory(CategoryRepository categoryRepository) {
            this.categoryRepository = categoryRepository;
        }

        @Override
        public <T extends jakarta.validation.ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            if (key == CategoryIdValidator.class) {
                return (T) new CategoryIdValidator(categoryRepository);
            }
            try {
                return key.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void releaseInstance(jakarta.validation.ConstraintValidator<?, ?> instance) {
            // No-op
        }
    }
}
