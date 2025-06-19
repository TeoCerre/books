package it.uniroma3.siw.SiwBooks.repository;

import it.uniroma3.siw.SiwBooks.model.BookImage;
import org.springframework.data.repository.CrudRepository;

public interface BookImageRepository extends CrudRepository<BookImage, Long> {
}