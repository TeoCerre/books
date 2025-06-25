package it.uniroma3.siw.SiwBooks.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.BookImage;
import it.uniroma3.siw.SiwBooks.model.User;
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

    @Transactional
    public Double getAverageRatingForBook(Long bookId) {
        return reviewRepository.findAverageRatingByBookId(bookId);
    }

    @Transactional
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksToRereadForUser(User user) {
        if (user != null) {
            return this.findBooksReviewedByUser(user.getId());
        }
        return List.of();
    }

    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void delete(Book book) {
        this.bookRepository.delete(book);
    }

    @Transactional
    public List<Book> findTop5BooksByAverageRating() {
        return bookRepository.findTopBooksOrderByAverageRating(PageRequest.of(0, 5));
    }

    @Transactional
    public Book findByIdWithReviews(Long id) {
        return bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Transactional
    public byte[] getImageDataById(Long imageId) {
        return bookImageRepository.findById(imageId).orElseThrow().getImageData();
    }

    @Transactional
    public Long deleteImageById(Long imageId) {
        BookImage img = bookImageRepository.findById(imageId).orElseThrow();
        Long bookId = img.getBook().getId();
        bookImageRepository.delete(img);
        return bookId;
    }

    @Transactional
    public BookImage findImageById(Long id) {
        return bookImageRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteImage(BookImage image) {
        bookImageRepository.delete(image);
    }

    @Transactional
    public List<Book> findBooksReviewedByUser(Long userId) {
        return reviewRepository.findBooksReviewedByUser(userId);
    }

    @Transactional(readOnly = true)
    public Map<Long, Double> getAverageRatingsForBooks(List<Book> books) {
        Map<Long, Double> averageRatings = new HashMap<>();
        for (Book book : books) {
            Double avg = this.getAverageRatingForBook(book.getId());
            averageRatings.put(book.getId(), avg != null ? avg : 0.0);
        }
        return averageRatings;
    }

    @Transactional(readOnly = true)
    public Map<Long, String> getFirstReviewTextsForBooks(List<Book> books) {
        Map<Long, String> firstReviewTexts = new HashMap<>();
        for (Book book : books) {
            if (book.getReviews() != null && !book.getReviews().isEmpty()) {
                String text = book.getReviews().stream()
                        .findFirst()
                        .map(r -> r.getText())
                        .orElse("Nessuna recensione");
                firstReviewTexts.put(book.getId(), text);
            } else {
                firstReviewTexts.put(book.getId(), "Nessuna recensione");
            }
        }
        return firstReviewTexts;
    }

}