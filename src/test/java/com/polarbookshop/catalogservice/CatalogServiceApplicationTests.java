package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expected = new Book("0306406152", "Title", "Author", 9.90);

		webTestClient
				.post().uri("/books")
				.bodyValue(expected)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual).isNotNull();
					assertThat(actual.isbn()).isEqualTo(expected.isbn());
				});
	}


	// new test for put new book 201
	@Test
	void whenPutNewBookThenShouldReturn201() {
		var book = new Book("9780306406157", "New Book", "Author", 9.90);

		webTestClient
				.put().uri("/books/" + book.isbn())
				.bodyValue(book)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class)
				.value(actual -> assertThat(actual).isEqualTo(book));
	}


	// new test for put on existing book for update 200
	@Test
	void whenPutExistingBookThenShouldReturn200() {
		var original = new Book("9780131103627", "Original Title", "Author", 9.90);

		webTestClient
				.post().uri("/books")
				.bodyValue(original)
				.exchange()
				.expectStatus().isCreated();

		var updated = new Book("9780131103627", "Updated Title", "Author", 12.50);

		webTestClient
				.put().uri("/books/" + updated.isbn())
				.bodyValue(updated)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.value(actual -> assertThat(actual).isEqualTo(updated));
	}

	// new test for mismatched isbn
	@Test
	void whenPutWithMismatchedIsbnThenShouldReturn400() {
		var book = new Book("9780306406157", "Title", "Author", 9.90);

		webTestClient
				.put().uri("/books/9780131103627")
				.bodyValue(book)
				.exchange()
				.expectStatus().isBadRequest();
	}

}
