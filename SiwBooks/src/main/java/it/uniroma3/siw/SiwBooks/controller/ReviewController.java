package it.uniroma3.siw.SiwBooks.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.Review;
import it.uniroma3.siw.SiwBooks.model.User;
import it.uniroma3.siw.SiwBooks.service.BookService;
import it.uniroma3.siw.SiwBooks.service.ReviewService;
import it.uniroma3.siw.SiwBooks.service.UserService;

@Controller
public class ReviewController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/books/{id}/review")
    public String addReview(@PathVariable Long id,
                             @Valid @ModelAttribute("review") Review review,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {

        Book book = bookService.findById(id);
        if (book == null) {
            return "error/404";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);             
            model.addAttribute("review", review);         
            return "bookDetails";                         
        }

        User user = userService.getUserByUsername(userDetails.getUsername());

        review.setBook(book);
        review.setAuthor(user);
        reviewService.save(review);

        return "redirect:/books/" + id;
    }
}
