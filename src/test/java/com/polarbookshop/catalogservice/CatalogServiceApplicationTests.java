package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private BookRepository bookRepository;

	@AfterEach
	void cleanUp() {
		bookRepository.deleteAll();
	}

	private void assertBookDetails(Book actual, Book expected) {
		assertThat(actual).isNotNull();
		assertThat(actual.isbn()).isEqualTo(expected.isbn());
		assertThat(actual.title()).isEqualTo(expected.title());
		assertThat(actual.author()).isEqualTo(expected.author());
		assertThat(actual.price()).isEqualTo(expected.price());
		assertThat(actual.publisher()).isEqualTo(expected.publisher());
		assertThat(actual.id()).isNotNull();
		assertThat(actual.createdDate()).isNotNull();
		assertThat(actual.lastModifiedDate()).isNotNull();
	}

	@Test
	void whenPostRequestThenBookCreated() {
		var expected = Book.of("0306406152", "Title", "Author", 9.90, null);

		webTestClient
				.post().uri("/books")
				.bodyValue(expected)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class)
				.value(actual -> assertBookDetails(actual, expected));
	}

	@Test
	void whenPutNewBookThenShouldReturn201() {
		var book = Book.of("9780306406157", "New Book", "Author", 9.90, null);

		webTestClient
				.put().uri("/books/" + book.isbn())
				.bodyValue(book)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class)
				.value(actual -> assertBookDetails(actual, book));
	}

	@Test
	void whenPutExistingBookThenShouldReturn200() {
		var original = Book.of("9780131103627", "Original Title", "Author", 9.90, null);

		webTestClient
				.post().uri("/books")
				.bodyValue(original)
				.exchange()
				.expectStatus().isCreated();

		var updated = Book.of("9780131103627", "Updated Title", "Author", 12.50, null);

		webTestClient
				.put().uri("/books/" + updated.isbn())
				.bodyValue(updated)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.value(actual -> assertBookDetails(actual, updated));
	}

	@Test
	void whenPutWithMismatchedIsbnThenShouldReturn400() {
		var book = Book.of("9780306406157", "Title", "Author", 9.90, null);

		webTestClient
				.put().uri("/books/9780131103627")
				.bodyValue(book)
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void whenBookLifecycleCompletedThenShouldReturnCorrectStatuses() {
		String isbn = "9780201633610";
		var original = Book.of(isbn, "Original Title", "Author", 9.90, "Polar Publishing");
		// POST: Create the book
		webTestClient
				.post().uri("/books")
				.bodyValue(original)
				.exchange()
				.expectStatus().isCreated();

		// GET: Retrieve the book
		webTestClient
				.get().uri("/books/" + isbn)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.value(actual -> assertBookDetails(actual, original));

		// PUT: Update the book
		var updated = Book.of(isbn, "Updated Title", "Author", 12.50, "Updated Publisher");
		webTestClient
				.put().uri("/books/" + isbn)
				.bodyValue(updated)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.value(actual -> assertBookDetails(actual, updated));

		// DELETE: Remove the book
		webTestClient
				.delete().uri("/books/" + isbn)
				.exchange()
				.expectStatus().isNoContent();

		// GET: Verify the book no longer exists
		webTestClient
				.get().uri("/books/" + isbn)
				.exchange()
				.expectStatus().isNotFound();
	}
}
