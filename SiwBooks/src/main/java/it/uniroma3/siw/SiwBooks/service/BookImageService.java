package it.uniroma3.siw.SiwBooks.service;

import it.uniroma3.siw.SiwBooks.model.BookImage;
import it.uniroma3.siw.SiwBooks.repository.BookImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookImageService {

    @Autowired
    private BookImageRepository bookImageRepository;

    public Optional<BookImage> findById(Long id) {
        return bookImageRepository.findById(id);
    }

    public BookImage save(BookImage image) {
        return bookImageRepository.save(image);
    }
}