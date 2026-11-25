
package com.polarbookshop.catalogservice.domain;

import java.util.Optional;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExisting() {
        var isbn = "0306406152";
        var book = Book.of(isbn, "Title", "Author", 12.90, null);

        jdbcAggregateTemplate.insert(book);

        Optional<Book> actual = bookRepository.findByIsbn(isbn);

        assertThat(actual).isPresent();
        assertThat(actual.get().isbn()).isEqualTo(isbn);
    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        assertThat(bookRepository.findByIsbn("9780306406157"))
                .isEmpty();
    }

    @Test
    void findAllBooks() {
        var book1 = Book.of("0306406152", "Title One", "Author", 9.90, null);
        var book2 = Book.of("9780306406157", "Title Two", "Author", 12.90, null);

        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        assertThat(bookRepository.findAll())
                .extracting(Book::isbn)
                .contains(book1.isbn(), book2.isbn());
    }

    @Test
    void existsByIsbnWhenExisting() {
        var isbn = "0306406152";
        var book = Book.of(isbn, "Title", "Author", 9.90, null);

        jdbcAggregateTemplate.insert(book);

        assertThat(bookRepository.existsByIsbn(isbn)).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting() {
        assertThat(bookRepository.existsByIsbn("9780306406157"))
                .isFalse();
    }

    @Test
    void deleteByIsbnWhenExisting() {
        var isbn = "0306406152";
        var book = Book.of(isbn, "Title", "Author", 9.90, null);

        jdbcAggregateTemplate.insert(book);

        bookRepository.deleteByIsbn(isbn);

        assertThat(bookRepository.findByIsbn(isbn)).isEmpty();
    }

    @Test
    void whenBookSavedThenCreatedDateIsSet() {
        var book = Book.of("0306406152", "Title", "Author", 9.90, null);

        var savedBook = bookRepository.save(book);

        assertThat(savedBook.createdDate()).isNotNull();
    }


    @Test
    void whenPublisherDefinedThenPublisherIsPersisted() {
        var book = Book.of(
                "0306406152",
                "Title",
                "Author",
                9.90,
                "Polar Publishing"
        );

        bookRepository.save(book);

        var actual = bookRepository.findByIsbn(book.isbn());

        assertThat(actual).isPresent();
        assertThat(actual.get().publisher())
                .isEqualTo("Polar Publishing");
    }

    @Test
    void whenPublisherMissingThenBookIsStillPersisted() {
        var book = Book.of(
                "0306406152",
                "Title",
                "Author",
                9.90,
                null
        );

        bookRepository.save(book);

        var actual = bookRepository.findByIsbn(book.isbn());

        assertThat(actual).isPresent();
        assertThat(actual.get().publisher()).isNull();
    }

}
