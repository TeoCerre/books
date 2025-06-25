package it.uniroma3.siw.SiwBooks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.User;
import it.uniroma3.siw.SiwBooks.service.BookService;
import it.uniroma3.siw.SiwBooks.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        List<Book> libri = bookService.findAllBooks();
        model.addAttribute("libri", libri);

        model.addAttribute("averageRatings", bookService.getAverageRatingsForBooks(libri));
        model.addAttribute("firstReviewTexts", bookService.getFirstReviewTextsForBooks(libri));

        List<Book> topBooks = bookService.findTop5BooksByAverageRating();
        model.addAttribute("topBooks", topBooks);

        User user = userService.getAuthenticatedUser();
        if (user != null) {
            List<Book> rereadBooks = bookService.getBooksToRereadForUser(user);
            model.addAttribute("rereadBooks", rereadBooks);
        }

        return "home";
    }

}
