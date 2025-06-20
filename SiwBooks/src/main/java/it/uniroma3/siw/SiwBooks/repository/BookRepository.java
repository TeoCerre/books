package it.uniroma3.siw.SiwBooks.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import it.uniroma3.siw.SiwBooks.model.*;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll();

    Book findByTitle(String title);

    @Query("""
                SELECT b FROM Book b
                LEFT JOIN FETCH b.authors
                LEFT JOIN FETCH b.reviews r
                LEFT JOIN FETCH r.author
                WHERE b.id = :id
            """)
    Optional<Book> findByIdWithAllDetails(@Param("id") Long id);

    @Query("""
                SELECT b FROM Book b
                LEFT JOIN b.reviews r
                GROUP BY b
                ORDER BY COALESCE(AVG(r.rating), 0) DESC
            """)
    List<Book> findTopBooksOrderByAverageRating(Pageable pageable);

    @Query("""
                SELECT b FROM Book b
                LEFT JOIN FETCH b.images
                WHERE b.id = :id
            """)
    Optional<Book> findByIdWithImages(@Param("id") Long id);

}