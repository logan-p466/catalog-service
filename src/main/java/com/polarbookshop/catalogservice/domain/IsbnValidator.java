package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null || isbn.isBlank()) {
            return true; // @NotBlank handles missing ISBNs
        }

        // ISBN-10
        if (isbn.matches("[0-9]{9}[0-9Xx]")) {
            int sum = 0;

            for (int i = 0; i < 9; i++) {
                sum += (10 - i) * (isbn.charAt(i) - '0');
            }

            char last = Character.toUpperCase(isbn.charAt(9));
            int checkDigit = last == 'X' ? 10 : last - '0';

            sum += checkDigit;

            return sum % 11 == 0;
        }

        // ISBN-13
        if (isbn.matches("[0-9]{13}")) {
            int sum = 0;

            for (int i = 0; i < 13; i++) {
                int digit = isbn.charAt(i) - '0';
                sum += digit * (i % 2 == 0 ? 1 : 3);
            }

            return sum % 10 == 0;
        }

        return false;
    }
}
