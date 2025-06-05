package it.uniroma3.siw.SiwBooks.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.SiwBooks.model.*;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll();

}