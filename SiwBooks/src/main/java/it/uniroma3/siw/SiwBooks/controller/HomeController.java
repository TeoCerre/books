package it.uniroma3.siw.SiwBooks.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

        Map<Long, Double> averageRatings = new HashMap<>();
        Map<Long, String> firstReviewTexts = new HashMap<>();

        for (Book libro : libri) {
            Double avg = bookService.getAverageRatingForBook(libro.getId());
            averageRatings.put(libro.getId(), avg != null ? avg : 0.0);

            // Ottieni la prima recensione se presente
            if (libro.getReviews() != null && !libro.getReviews().isEmpty()) {
                String firstReviewText = libro.getReviews().stream()
                        .findFirst()
                        .map(r -> r.getText())
                        .orElse("Nessuna recensione");
                firstReviewTexts.put(libro.getId(), firstReviewText);
            } else {
                firstReviewTexts.put(libro.getId(), "Nessuna recensione");
            }
        }

        model.addAttribute("averageRatings", averageRatings);
        model.addAttribute("firstReviewTexts", firstReviewTexts);

        List<Book> topBooks = bookService.findTop5BooksByAverageRating();
        model.addAttribute("topBooks", topBooks);

        // Se utente autenticato, aggiungi libri da rileggere
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                List<Book> rereadBooks = bookService.findBooksReviewedByUser(user.getId());
                model.addAttribute("rereadBooks", rereadBooks);
            }
        }

        return "home";
    }

}
