package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookAlreadyExistsException;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(BookController.class)
public class BookControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "7373731394";
        given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mockMvc.perform(get("/books/" + isbn))
                .andExpect(status().isNotFound());
    }

    // 422, duplicate ISBN
    @Test
    void whenPostBookAlreadyExistsThenShouldReturn422() throws Exception {
        String isbn = "0306406152";

        given(bookService.addBookToCatalog(any(Book.class)))
            .willThrow(new BookAlreadyExistsException(isbn));

        String json = """
                {
                    "isbn": "0306406152",
                    "title": "Title",
                    "author": "Author",
                    "price": 9.90
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    // 400, invalid request body
    @Test
    void whenPostBookInvalidThenShouldReturn400() throws Exception {
        String json = """
                {
                    "isbn": "0306406152",
                    "title": "",
                    "author": "Author",
                    "price": 9.90
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    void whenGetBooksByAuthorThenShouldReturnMatchingBooks() throws Exception {
        String author = "Author One";

        var book = Book.of(
                "0306406152",
                "Title",
                author,
                9.90,
                null
        );

        given(bookService.viewBooksByAuthor(author))
                .willReturn(List.of(book));

        mockMvc.perform(get("/books")
                        .param("author", author))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].isbn").value(book.isbn()))
                .andExpect(jsonPath("$[0].author").value(author));
    }

    @Test
    void whenGetBooksByUnknownAuthorThenShouldReturnEmptyList() throws Exception {
        String author = "Unknown Author";

        given(bookService.viewBooksByAuthor(author))
                .willReturn(List.of());

        mockMvc.perform(get("/books")
                        .param("author", author))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void whenGetBooksWithoutAuthorThenShouldReturnAllBooks() throws Exception {
        var book1 = Book.of("0306406152", "Title One", "Author One", 9.90, null);
        var book2 = Book.of("9780306406157", "Title Two", "Author Two", 12.90, null);

        given(bookService.viewBookList())
                .willReturn(List.of(book1, book2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


}
