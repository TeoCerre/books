package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.service.AuthorService;
import it.uniroma3.siw.SiwBooks.service.BookService;
import it.uniroma3.siw.SiwBooks.service.ReviewService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ReviewService reviewService; 
    
    @GetMapping("/home")
    public String adminHome() {
        return "admin/adminHome";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        return "admin/newBook";
    }

    @GetMapping("/books")
    public String adminBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "admin/bookList";
    }

    @GetMapping("/authors/new")
    public String newAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "admin/newAuthor";
    }

    @GetMapping("/autori")
    public String adminAuthors(Model model) {
        model.addAttribute("autori", authorService.findAll());
        return "admin/authorList";
    }

    @GetMapping("/reviews/delete")
    public String deleteReviews(Model model) {
        // Assumi che ReviewService sia autowired
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/reviewDelete";
    }
}
