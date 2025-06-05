package it.uniroma3.siw.SiwBooks.config;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.repository.BookRepository;
import it.uniroma3.siw.SiwBooks.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Autowired
    private BookService bookService;

    @Bean
    CommandLineRunner loadImages() {
        return args -> {
            // Titoli e file immagini corrispondenti
            Map<String, String> booksImages = Map.of(
                "L amica geniale", "L amica geniale.jpeg",
                "White Noise", "White Noise.jpeg",
                "Atonement", "Atonement.jpeg",
                "Io non ho paura", "Io non ho paura.jpeg",
                "The Corrections", "The Corrections.jpeg"
            );

            for (Map.Entry<String, String> entry : booksImages.entrySet()) {
                String title = entry.getKey();
                String fileName = entry.getValue();

                Book book = bookService.findByTitle(title);
                if (book != null) {
                    byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/static/images/" + fileName));
                    book.setCoverImage(imageBytes);
                    bookService.save(book);
                    System.out.println("Immagine caricata per: " + title);
                } else {
                    System.out.println("Libro non trovato: " + title);
                }
            }
        };
    }
}

