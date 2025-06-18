package it.uniroma3.siw.SiwBooks.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.service.AuthorService;
import it.uniroma3.siw.SiwBooks.service.BookService;

@RestController
public class BookRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    // Crea un nuovo libro (solo admin)
    @PostMapping("/admin/rest/books")
    public Book newBook(@Valid @RequestBody Book book) {
        this.bookService.save(book);
        return book;
    }

    // Recupera tutti i libri
    @GetMapping("/rest/books")
    public List<Book> getBooks() {
        return new ArrayList<>(this.bookService.findAllBooks());
    }

    // Recupera un libro per ID
    @GetMapping("/rest/books/{id}")
    public Book getBook(@PathVariable("id") Long id) {
        return this.bookService.findById(id);
    }

    // Assegna un autore a un libro
    @PutMapping("/admin/rest/books/{bookId}/authors/{authorId}")
    public Book addAuthorToBook(@PathVariable("bookId") Long bookId,
                                @PathVariable("authorId") Long authorId) {
        Book book = this.bookService.findById(bookId);
        Author author = this.authorService.findById(authorId);
        Set<Author> authors = book.getAuthors();
        authors.add(author);
        this.bookService.save(book);
        return book;
    }

    // Rimuovi un autore da un libro
    @DeleteMapping("/admin/rest/books/{bookId}/authors/{authorId}")
    public Book removeAuthorFromBook(@PathVariable("bookId") Long bookId,
                                     @PathVariable("authorId") Long authorId) {
        Book book = this.bookService.findById(bookId);
        Author author = this.authorService.findById(authorId);
        book.getAuthors().remove(author);
        this.bookService.save(book);
        return book;
    }

    // // Modifica un libro esistente
    // @PutMapping("/admin/rest/books/{id}")
    // public Book updateBook(@PathVariable("id") Long id, @Valid @RequestBody Book updatedBook) {
    //     Book book = this.bookService.findById(id);
    //     book.setTitle(updatedBook.getTitle());
    //     book.setYear(updatedBook.getYear());
    //     book.setCoverImage(updatedBook.getCoverImage());
    //     return this.bookService.save(book);
    // }

    // Elimina un libro
    @DeleteMapping("/admin/rest/books/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        Book book = this.bookService.findById(id);
        this.bookService.delete(book);
    }
}
