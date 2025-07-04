package it.uniroma3.siw.SiwBooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Author findByNameAndSurname(String name, String surname) {
        return authorRepository.findByNameAndSurname(name, surname);
    }

    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Transactional
    public List<Author> findAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    @Transactional
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
    }

    @Transactional
    public Author findByIdWithBooks(Long id) {
        return authorRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
    }

    @Transactional
    public byte[] getPhotoById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        return author.getPhoto();
    }

}
