package it.uniroma3.siw.SiwBooks.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.SiwBooks.model.*;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}