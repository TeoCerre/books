package it.uniroma3.siw.SiwBooks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.repository.BookRepository;
import it.uniroma3.siw.SiwBooks.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Double getAverageRatingForBook(Long bookId) {
        return reviewRepository.findAverageRatingByBookId(bookId);
    }

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

    public void delete(Book book) {
        this.bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public Book findByIdWithReviews(Long id) {
        return bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

}