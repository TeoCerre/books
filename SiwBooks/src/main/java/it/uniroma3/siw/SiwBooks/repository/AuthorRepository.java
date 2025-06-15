package it.uniroma3.siw.SiwBooks.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.SiwBooks.model.*;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findByNameAndSurname(String name, String surname);
    
    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :id")
    Optional<Author> findByIdWithBooks(Long id);
}