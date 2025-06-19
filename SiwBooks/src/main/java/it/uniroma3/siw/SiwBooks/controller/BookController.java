package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.Review;
import it.uniroma3.siw.SiwBooks.model.User;
import it.uniroma3.siw.SiwBooks.service.BookService;
import it.uniroma3.siw.SiwBooks.service.ReviewService;
import it.uniroma3.siw.SiwBooks.service.UserService;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/books/{id}/cover")
    public ResponseEntity<byte[]> getBookCover(@PathVariable Long id) {
        try {
            System.out.println("👉 Richiesta cover per libro ID: " + id);
            Book book = bookService.findById(id);
            if (book == null) {
                System.out.println("❌ Libro non trovato");
                return ResponseEntity.notFound().build();
            }

            byte[] image = book.getCoverImage();
            if (image == null || image.length == 0) {
                System.out.println("⚠️ Nessuna immagine trovata");
                return ResponseEntity.notFound().build();
            }

            System.out.println("✅ Cover trovata: dimensione = " + image.length + " byte");

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/books/{id}")
    public String getBookDetails(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookService.findByIdWithReviews(id);
        if (book == null) {
            return "error";
        }

        model.addAttribute("book", book);
        model.addAttribute("review", new Review());

        boolean alreadyReviewed = false;
        if (userDetails != null) {
            User currentUser = userService.findByUsername(userDetails.getUsername());
            // qui currentUser potrebbe essere null, quindi serve controllo:
            if (currentUser != null) {
                alreadyReviewed = reviewService.hasUserReviewedBook(currentUser.getId(), book.getId());
            }
        }

        model.addAttribute("alreadyReviewed", alreadyReviewed);

        return "bookDetails";
    }

}
