package com.polarbookshop.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Book (
    // new isbn validation
    @NotBlank(message = "The book ISBN must be defined.")
    @ValidIsbn
    String isbn,

    @NotBlank(message = "The title must be defined.")
    String title,

    @NotBlank(message = "The author must be defined.")
    String author,

    @NotNull(message = "The book price must be defined.")
    @Positive(message = "The book price must be greater than zero.")
    Double price
) {}
