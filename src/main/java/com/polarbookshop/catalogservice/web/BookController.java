package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> get() {
        return bookService.viewBookList();
    }

    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book post(@Valid @RequestBody Book book) {
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }


    // update put to distinguish edit vs create
    // updated to add ISBN check before calling bookService
    @PutMapping("{isbn}")
    public ResponseEntity<Book> put(@PathVariable String isbn, @Valid @RequestBody Book book) {
        if (!isbn.equals(book.isbn())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The ISBN in the URL must match the ISBN in the request body."
            );
        }

        boolean exists = bookService.bookExists(isbn);
        Book savedBook = bookService.editBookDetails(isbn, book);

        if (exists) {
            return ResponseEntity.ok(savedBook);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        }
    }
}
