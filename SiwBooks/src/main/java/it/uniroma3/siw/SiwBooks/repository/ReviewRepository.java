package it.uniroma3.siw.SiwBooks.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.SiwBooks.model.*;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.book.id = :bookId AND r.author.id = :userId")
	boolean existsByBookIdAndAuthorId(@Param("bookId") Long bookId, @Param("userId") Long userId);
	
    @Query("SELECT r.book FROM Review r WHERE r.author.id = :userId")
    java.util.List<Book> findBooksReviewedByUser(@Param("userId") Long userId);

}