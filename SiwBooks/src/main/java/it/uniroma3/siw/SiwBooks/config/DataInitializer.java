package it.uniroma3.siw.SiwBooks.config;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.BookImage;
import it.uniroma3.siw.SiwBooks.service.BookService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Autowired
    private BookService bookService;

    @Bean
    @Transactional
    CommandLineRunner loadImages() {
        return args -> {
            Map<String, String> booksImages = Map.of(
                    "L amica geniale", "L amica geniale.jpeg",
                    "White Noise", "White Noise.jpeg",
                    "Atonement", "Atonement.jpeg",
                    "Io non ho paura", "Io non ho paura.jpeg",
                    "The Corrections", "The Corrections.jpeg",
                    "Anna", "Anna.jpeg",
                    "Freedom", "Freedom.jpeg",
                    "Underworld", "Underworld.jpeg");

            for (Map.Entry<String, String> entry : booksImages.entrySet()) {
                String title = entry.getKey();
                String coverFileName = entry.getValue();

                Book book = bookService.findByTitle(title);
                if (book != null) {
                    // Lettura e set cover principale
                    byte[] coverBytes = Files
                            .readAllBytes(Paths.get("src/main/resources/static/images/" + coverFileName));
                    book.setCoverImage(coverBytes);

                    // Assicurati che il set images sia inizializzato
                    if (book.getImages() == null) {
                        book.setImages(new HashSet<>());
                    }

                    // Carica immagini aggiuntive numerate (Title1.jpeg, Title2.jpeg, ...)
                    int index = 1;
                    while (true) {
                        String extraFileName = String.format("%s%d.jpeg", title, index);
                        java.nio.file.Path extraPath = Paths.get("src/main/resources/static/images/" + extraFileName);

                        if (!Files.exists(extraPath))
                            break;

                        byte[] extraBytes = Files.readAllBytes(extraPath);
                        BookImage extraImage = new BookImage();
                        extraImage.setBook(book);
                        extraImage.setImageData(extraBytes);

                        book.getImages().add(extraImage);
                        index++;
                    }

                    bookService.save(book);
                    System.out.println("Immagini caricate per: " + title);
                } else {
                    System.out.println("Libro non trovato: " + title);
                }
            }
        };
    }

}