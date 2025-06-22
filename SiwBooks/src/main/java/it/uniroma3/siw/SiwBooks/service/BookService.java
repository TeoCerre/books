package it.uniroma3.siw.SiwBooks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.BookImage;
import it.uniroma3.siw.SiwBooks.repository.BookRepository;
import it.uniroma3.siw.SiwBooks.repository.BookImageRepository;
import it.uniroma3.siw.SiwBooks.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookImageRepository bookImageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Double getAverageRatingForBook(Long bookId) {
        return reviewRepository.findAverageRatingByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public void delete(Book book) {
        this.bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<Book> findTop5BooksByAverageRating() {
        return bookRepository.findTopBooksOrderByAverageRating(PageRequest.of(0, 5));
    }

    @Transactional(readOnly = true)
    public Book findByIdWithReviews(Long id) {
        return bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Transactional(readOnly = true)
    public byte[] getImageDataById(Long imageId) {
        return bookImageRepository.findById(imageId).orElseThrow().getImageData();
    }

    @Transactional(readOnly = true)
    public Long deleteImageById(Long imageId) {
        BookImage img = bookImageRepository.findById(imageId).orElseThrow();
        Long bookId = img.getBook().getId();
        bookImageRepository.delete(img);
        return bookId;
    }

    @Transactional(readOnly = true)
    public BookImage findImageById(Long id) {
        return bookImageRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public void deleteImage(BookImage image) {
        bookImageRepository.delete(image);
    }

    @Transactional(readOnly = true)
    public List<Book> findBooksReviewedByUser(Long userId) {
        return reviewRepository.findBooksReviewedByUser(userId);
    }

}