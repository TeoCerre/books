package it.uniroma3.siw.SiwBooks.controller.rest;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.Review;
import it.uniroma3.siw.SiwBooks.service.AuthorService;
import it.uniroma3.siw.SiwBooks.service.BookService;
import it.uniroma3.siw.SiwBooks.service.ReviewService;

@RestController
public class BookRestController {

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/admin/rest/books")
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.save(book);
    }

    @GetMapping("/rest/books")
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/rest/books/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PutMapping("/admin/rest/books/{id}")
    public Book updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        book.setId(id);
        return bookService.save(book);
    }

    @DeleteMapping("/admin/rest/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }
     @PostMapping("/admin/rest/autori")
    public Author createAuthor(@Valid @RequestBody Author author) {
        return authorService.save(author);
    }

    @GetMapping("/rest/autori")
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping("/rest/autori/{id}")
    public Author getAuthor(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @PutMapping("/admin/rest/autori/{id}")
    public Author updateAuthor(@PathVariable Long id, @Valid @RequestBody Author author) {
        author.setId(id);
        return authorService.save(author);
    }

    @DeleteMapping("/admin/rest/autori/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);
    }   
    @GetMapping("/admin/rest/reviews")
    public List<Review> getAllReviews() {
        return reviewService.findAll();
    }

    @DeleteMapping("/admin/rest/reviews/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
    }
}
