package it.uniroma3.siw.SiwBooks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

}