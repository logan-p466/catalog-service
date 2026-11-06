package com.polarbookshop.catalogservice.domain;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var book = new Book("0306406152", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        var book = new Book("a234567890", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    void whenTitleBlankThenValidationFails() {
        var book = new Book("0306406152", "", "Author", 9.90);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The title must be defined.");
    }

    @Test
    void whenAuthorBlankThenValidationFails() {
        var book = new Book("0306406152", "Title", "", 9.90);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The author must be defined.");
    }

    @Test
    void whenPriceMissingThenValidationFails() {
        var book = new Book("0306406152", "Title", "Author", null);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be defined.");
    }

    @Test
    void whenPriceZeroThenValidationFails() {
        var book = new Book("0306406152", "Title", "Author", 0.0);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

    // new tests for ISBN
    @Test
    void whenIsbn10ValidThenValidationSucceeds() {
        var book = new Book("0306406152", "Title", "Author", 9.90);

        assertThat(validator.validate(book)).isEmpty();
    }

    @Test
    void whenIsbn10EndsWithXThenValidationSucceeds() {
        var book = new Book("080442957X", "Title", "Author", 9.90);

        assertThat(validator.validate(book)).isEmpty();
    }

    @Test
    void whenIsbn10CheckDigitIncorrectThenValidationFails() {
        var book = new Book("0306406153", "Title", "Author", 9.90);

        var violations = validator.validate(book);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    void whenIsbn13CheckDigitIncorrectThenValidationFails() {
        var book = new Book("9780306406158", "Title", "Author", 9.90);

        var violations = validator.validate(book);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

}
