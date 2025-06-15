package it.uniroma3.siw.SiwBooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author findByNameAndSurname(String name, String surname) {
        return authorRepository.findByNameAndSurname(name, surname);
    }

    public void save(Author author) {
        authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public Author findByIdWithBooks(Long id) {
        return authorRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
    }

    @Transactional(readOnly = true)
    public byte[] getPhotoById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        return author.getPhoto();
    }

}
