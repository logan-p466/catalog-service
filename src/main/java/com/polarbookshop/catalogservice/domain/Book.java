package com.polarbookshop.catalogservice.domain;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

public record Book(
        @Id
        Long id,

        @NotBlank(message = "The book ISBN must be defined.")
        @ValidIsbn
        String isbn,

        @NotBlank(message = "The title must be defined.")
        String title,

        @NotBlank(message = "The author must be defined.")
        String author,

        @NotNull(message = "The book price must be defined.")
        @Positive(message = "The book price must be greater than zero.")
        Double price,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @Version
        int version
) {
    public static Book of(String isbn, String title, String author, Double price) {
        return new Book(null, isbn, title, author, price, null, null, 0);
    }
}
