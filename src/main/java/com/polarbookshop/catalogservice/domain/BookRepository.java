package com.polarbookshop.catalogservice.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
// no longer need to implement save(), findAll(), or deleteAll() yourself. CrudRepository provides those operations.
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Modifying
    @Transactional
    @Query("delete from book where isbn = :isbn")
    void deleteByIsbn(String isbn);
}
